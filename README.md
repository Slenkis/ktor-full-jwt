# ktor-full-jwt

## 👏 О чём этот проект? 
Проект создан как пример полноценной реализации JWT аутентефикации с помощью Ktor и Exposed.

## 🙊 Что умеет?
✔️Аутентефикация пользователя через API по логину и паролю.  
✔️Выдача refresh и access токенов. Их обновление.  
✖️Страницы входа и регистрации в процессе, скоро будет пример по фронту.

## 🏁 Что нужно для старта?
- Ktor  
- Exposed  
- База данных (PostgreSQL)  
- BCrypt  

1. Клонируем репозиторий: 
`git clone https://github.com/Slenkis/ktor-full-jwt.git`

2. Создаем 2 таблицы в БД: **refresh_tokens** и **users**  
![Таблицы](https://imgur.com/2SRHjyr.jpg)

3. В [application.conf](resources/application.conf) указываем данные от БД

4. Запускам проект: 
`./gradlew run`

## 🗿 Как пользоваться?

1. Отправляем на `/api/login` json-запрос:
```json
{
    "email": "example@gmail.com",
    "password": "qwerty123"
}
```

2. Если пользователь есть в БД, то в ответ получаем пару токенов:
```json
{
    "accessToken": "XXXXXX.YYYYYY.ZZZZZZ",
    "refreshToken": "d19e6fcd-ee18-4b38-acae-d1f7b9109118"
}
```

3. Выполняем запросы к нужным URL, предоставляя **access-токен** в заголовке *Authorization*:  
```Authorization: Bearer XXXXXX.YYYYYY.ZZZZZZ```

4. При истечении срока жизни **access-токена** необходимо получить новую пару *(её пример находится в пункте 2)*, отправив запрос на `/api/refresh`, в теле которого указать **refresh-токен**:
```json
{
    "refreshToken": "d19e6fcd-ee18-4b38-acae-d1f7b9109118"
}
```

## ⚡ Есть замечания или предложения?
Можно оформить [Issue](../../issues) или связаться со мной в [Telegram](https://t.me/slenkis)

## Лицензия
Проект имеет лицензию Apache 2.0, подробности в [LICENSE](LICENSE)