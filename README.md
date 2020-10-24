# ktor-full-jwt

# 📖 About 
The project was created as an example of a full-fledged implementation JWT authentication with [Ktor](https://ktor.io/) & [Exposed](https://github.com/JetBrains/Exposed).

# ⭐ Capabilities
✔ User authentication via API by the login and password.  
✔ Issuance of refresh and access tokens.  
✔ Updating refresh tokens.  
✖ Login and registration pages coming soon.

# 🚀 What need to start?
- Ktor  
- Exposed  
- Jackson  
- Database (PostgreSQL)  
- BCrypt  

1. Clone repository: 
`git clone https://github.com/Slenkis/ktor-full-jwt.git`

2. Create 2 tables in the database: **refresh_tokens** & **users**  
![Tables](https://imgur.com/2SRHjyr.jpg)

3. Change credentials for connecting to database in [application.conf](resources/application.conf)

4. Run project: 
`./gradlew run`

# 🍒 Usage
1. Send login request to `/api/login` with **email** and **password** in body.

2. Send a request to `/users` with the **access** token in the *Authorization* header.

3. When the **access** token lifetime expires, you need to get a new pair by sending a request to `/api/auth/refresh`, in the body of which specify the current **refresh** token.

# ✨ API

## 🔑 Login
Used to collect tokens for a registered user.

**URL** : `/api/login/`  

**Method** : `POST`  

**Data example** :
```json
{
    "email": "bob@gmail.com",
    "password": "qwerty123"
}
```

### Success response
**Code** : `200 OK`

**Content example** :
```json
{
    "accessToken": "XXXXXX.YYYYYY.ZZZZZZ",
    "refreshToken": "d19e6fcd-ee18-4b38-acae-d1f7b9109118"
}
```
### Error response

**Code** : `401 Unauthorized`

**Condition** : If 'email' and 'password' combination is wrong.

## 🔁 Refresh
**URL** : `/api/refresh/`  

**Method** : `POST`

**Data example** :
```json
{
    "refreshToken": "d19e6fcd-ee18-4b38-acae-d1f7b9109118"
}
```

### Success response
**Code** : `200 OK`

**Content example** :
```json
{
    "accessToken": "KKKKKK.NNNNNN.DDDDDD",
    "refreshToken": "b1349089-8f71-464f-a0fa-f2675252693e"
}
```

### Error response

**Code** : `400 Bad Request`

**Condition** : If the refresh token is invalid or expired.

**Content example** :
```json
{
    "description": "invalid token"
}
```

## 👥 Get users
Get all users from the database 'users'

**URL** : `/users/`  

**Method** : `GET`

**Auth required** : `Yes, in header`

### Success response
**Code** : `200 OK`

**Content example** :
```json
[    
    {
        "id": 1,
        "email": "bob@gmail.com",
        "password": "$2y$08$W62OarwjrPeu1n23FgEXhev2ZncUWMNwD5hF3QZ195PGZB1HkD3Zu",
        "active": true
    },
    {
        "id": 2,
        "email": "alice@yahoo.com",
        "password": "$2y$08$1eSVekbH33kkkUWMhfJC4uJV21oWxsZMPssojXJo6O0TyS5p9kIFG",
        "active": false
    }
]
```

### Error response

**Code** : `401 Unauthorized`

**Condition** : If the access token is invalid or expired.

# 🤝 Contributing
Pull requests are welcome. For major changes, please open an [issue](../../issues) first to discuss what you would like to change.
Please make sure to update tests as appropriate.

# 📝 License
Project is licensing under [Apache-2.0](LICENSE)
