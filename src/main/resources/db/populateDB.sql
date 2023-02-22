DELETE
FROM meals;
DELETE
FROM user_role;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);


INSERT INTO meals (user_id, description, calories, date_time)
VALUES (100000, 'Завтрак', 500, '2020-01-30 10:00'),
       (100000, 'Обед', 1000, '2020-01-30 13:00'),
       (100000, 'Ужин', 500, '2020-01-30 20:00'),
       (100000, 'Еда на граничное значение', 100, '2020-01-31 00:00'),
       (100000, 'Завтрак', 1000, '2020-01-31 10:00'),
       (100000, 'Обед', 500, '2020-01-31 13:00'),
       (100000, 'Ужин', 410, '2020-01-31 20:00'),
       (100001, 'Админ ланч', 510, '2015-06-01 14:00'),
       (100001, 'Админ ужин', 1500, '2015-06-01 21:00');