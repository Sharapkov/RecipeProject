# RecipeProject
Веб-сервер для сохранения и поиска учетных записей и их публикаций.

Для проекта используются: Java, Apache TomCat, Maven, Spring, Hibernate, REST, PostgreSQL

Сервер реализует POST, GET запросы и передает пользьзователю информацию, соответствующую запросу.

Для регистрации/авторизации пользователя используется JWT.

для запуска win в контейнере:
1. git clone ...
2. docker build -t recipe . (с точкой)
3. docker create volume db_recipe_volume
4. docker-compose up --build
