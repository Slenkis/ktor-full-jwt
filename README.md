# JWT authentication (access and refresh tokens) in Ktor

This project was created as an example of a full-fledged implementation of JWT(with access and refresh tokens) in Ktor.
Project contains registration by credentials, JWT authentication, refreshing expired token and auth-provided endpoints.

## Capabilities
* User authentication by the email and password;  
* JSON forms validation;  
* Registration of users;  
* Issuing a pair of a refresh and access tokens;  
* Automatic creation of missing tables in the database;  
* Refreshing a pair of tokens using a refresh token.  

## Dependencies
* [Kotlin](https://github.com/JetBrains/kotlin) `1.5.21`
* [Ktor server](https://github.com/ktorio/ktor) `1.6.2`
* [Exposed](https://github.com/JetBrains/Exposed) `0.32.1`
* [Logback](https://github.com/qos-ch/logback) `1.2.5`
* [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc) `0.36.0`
* [Bcrypt](https://github.com/ToxicBakery/bcrypt-mpp) `1.0.9`
* [Konform](https://github.com/konform-kt/konform) `0.3.0`
* [ShadowJar](https://github.com/johnrengelman/shadow) `6.1.0`

## Running
Clone the repository:
```
$ git clone https://github.com/Slenkis/ktor-full-jwt.git
```

Navigate to the repository folder:
```
$ cd ktor-full-jwt
```

Run application:
```
$ ./gradlew run
```

## Documentation
| Method | Endpoint            | Wiki page                                                       |
| :----: | ------------------- | --------------------------------------------------------------- |
| POST   | `/api/registration` | [User registration](../../wiki/Endpoints#user-registration)     |
| POST   | `/api/login`        | [User authentication](../../wiki/Endpoints#user-authentication) |
| POST   | `/api/refresh`      | [Refresh token](../../wiki/Endpoints#refresh-token)             |
| GET    | `/users/me`         | [Get user info](../../wiki/Endpoints#get-user-info)             |

### Read about configuration, table schemes and more in [Wiki](../../wiki/Home)

## Contributing
Pull requests are welcome. For major changes, please open an [issue](../../issues) first to discuss what you would like to change.
Please make sure to update tests as appropriate.

## License
Project is licensing under [Apache-2.0](LICENSE)
