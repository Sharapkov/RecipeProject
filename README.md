# RecipeProject
Веб-сервер для сохранения и поиска учетных записей и их публикаций.

Для проекта используются: Java, Apache TomCat, Gradle, Spring, Hibernate, REST, PostgreSQL

Сервер реализует POST, GET запросы и передает пользьзователю информацию, соответствующую запросу.

Для регистрации/авторизации пользователя используется JWT, в котором содержится субъект и отметка времени, по истечении которой токен не является действительным.

В нерелизной версии вся база данных PostgreSQL чистится после закрытия приложения.

Для работы с приложением нужно:
1. запустить сервер и запустить POSTMAN.

2. отправить  POST-запрос с телом вида:

{
    "firstname": "Aaa",
    "lastname": "Bbb",
    "email": "Aaa@mail.com",
    "password": "123456789"
}

на localhost:8080/api/v1/auth/register и скопировать полученный токен.

3. (по желанию) отправить POST-запрос с телом вида:
{
    "email": "Aaa@mail.com",
    "password": "123456789"
}
на localhost:8080/api/v1/auth/authenticate и скопировать полученый токен.

4. Отправить POST запрос с телом вида:
{
    "name": "tea",
    "description": "mea",
    "ingredients": ["whater", "sugar"],
    "directions": ["Just relax", "tea is relax"]
}
на localhost:8080/api/recipe/new при этом в окно Authorization необходимо выбрать Type - bearer token, а в поле token вставить скопированный ранее токен со страницы /register или /authenticate. В ответ вы получите ваш уникальный id вашего поста.

5. Поиск постов можно осуществить также с помощью auth-токена, GET запрос следует отправлять на localhost:8080/api/recipe/7 - где "7" id поста.
