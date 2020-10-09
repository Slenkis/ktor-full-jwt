package com.github.slenkis

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.SerializationFeature
import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.event.Level
import java.io.File
import java.time.Duration
import java.util.*

@KtorExperimentalAPI
fun Application.stringProperty(path: String): String =
    this.environment.config.property(path).getString()

@KtorExperimentalAPI
fun Application.longProperty(path: String): Long =
    stringProperty(path).toLong()

fun Long.withOffset(offset: Duration) = this + offset.toMillis()

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
fun Application.module() {
    val dbUrl = stringProperty("authDB.url")
    val dbDriver = stringProperty("authDB.driver")
    val dbUsername = stringProperty("authDB.username")
    val dbPassword = stringProperty("authDB.password")
    Database.connect(dbUrl, dbDriver, dbUsername, dbPassword)

    val issuer: String = stringProperty("jwt.issuer")
    val algorithm = Algorithm.HMAC256(stringProperty("jwt.access.secret"))

    val accessLifetime = longProperty("jwt.access.lifetime")    // minutes
    val refreshLifetime = longProperty("jwt.refresh.lifetime")  // days

    fun generateTokenPair(userId: Int, isUpdate: Boolean = false): TokenPair {
        val currentTime = System.currentTimeMillis()

        val accessToken = JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Date(currentTime.withOffset(Duration.ofMinutes(accessLifetime))))
            .withIssuer(issuer)
            .sign(algorithm)

        val refreshToken = UUID.randomUUID().toString()

        if (!isUpdate) {
            transaction {
                Tokens.insert {
                    it[Tokens.userId] = userId
                    it[Tokens.refreshToken] = refreshToken
                    it[expiresAt] = currentTime.withOffset(Duration.ofDays(refreshLifetime))
                }
            }
        }
        return TokenPair(accessToken, refreshToken)
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Authentication) {
        jwt("access") {
            verifier {
                makeJWTVerifier(algorithm, issuer)
            }

            validate { token ->
                if (token.payload.expiresAt.time > System.currentTimeMillis())
                    JWTPrincipal(token.payload)
                else null
            }
        }
    }

    routing {
        staticRoutes()

        post("/api/login") {
            val authUser = call.receive<LoginUser>()
            val user = getUserByEmail(authUser.email)

            if (user != null && Bcrypt.verify(authUser.password, user.password)) {
                val id = user.id

                val tokenPair = generateTokenPair(id)
                call.respond(tokenPair)
            } else
                call.respond(HttpStatusCode.Unauthorized)
        }

        post("/api/refresh") {
            val oldRefreshToken = call.receive<RefreshToken>().refreshToken
            val token = transaction {
                Tokens.select { Tokens.refreshToken eq oldRefreshToken }
                    .mapNotNull { it.toToken() }.singleOrNull()
            }
            val currentTime = System.currentTimeMillis()

            if (token != null && token.expiresAt > currentTime) {
                val tokenPair = generateTokenPair(token.userId, true)

                transaction {
                    Tokens.update({ Tokens.refreshToken eq oldRefreshToken }) {
                        it[refreshToken] = tokenPair.refreshToken
                        it[expiresAt] = currentTime.withOffset(Duration.ofDays(refreshLifetime))
                    }
                }

                call.respond(tokenPair)
            } else
                call.respond(HttpStatusCode.BadRequest, "Invalid token")
        }

        /** - **/

        authenticate("access") {
            get("/users") {
                call.respond(getAllUsers())
            }
        }
    }
}

fun Route.staticRoutes() {
    static("/") {
        staticRootFolder = File("resources/static/web")

        // HTML
        file("login", "login/login.html")
        file("registration", "login/registration.html")

        // CSS
        static("styles") {
            file("login.css", "login/login.css")
            file("registration.css", "login/registration.css")
            file("common-auth.css", "common-auth.css")
        }

        // JS
        static("scripts") {
            file("login.js", "login/login.js")
        }
    }
}