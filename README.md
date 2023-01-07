# TestTask
## Техническое задание 
Реализовать «Книжный Тиндер», а точнее его прототип, имеющий следующий ограниченный функционал:

●	свайп книг(свайп вправо - понравились, свайп влево - не понравились, вверх - хочу прочитать, вниз - пропустить (не читал)).,

●	возможность завершить работу со свайпом и перейти на экран результатов, где будет представлена статистика:количество прочитанных книг, количество понравившихся книг,
топ 3 жанра, прочитанных,

●	на странице статистики есть кнопка поделиться статистикой и окно для ввода телефона обратной связи (почты)

___
## Проблемы с которыми я столкнулся
Отсутствие баз данныз пользователей и книг. Непредсказуемость MotionLayout из-за чего с ним достаточно проблемно работать. Мерцание карточек с книгами по окончанию свайпа.
___
## Мое решение
Так как у меня не было доступа к базе данных пользователей, а в задании нужно выводить и обновлять статистику пользователя, я решил для имитации бд пользователей
создавать эту бд локально, использовав SQLite с библиотекой room, в этой бд хранится только один пользователь. Если пользователь заходит впервые в это приложение, то в бд создается новый пользователь со сгенерированным айди, которое сохраняется в хранилище dataStore. Когда пользователь снова заходит в приложение, то сначала проверяется на наличие данных в локальной бд, если данные есть, то они достаются по айди, которое сохранено в dataStore.
Так как у меня не было доступа к базе данных книг, то я решил воспользоваться сайтом jsonkeeper и сохранить там json объект, имитирующий примерные данные по книгам.
Чтобы делать get запросы, для получения json объекта, я воспользовался библиотекой retrofit, а для того чтобы десериализовывать этот json объект в объекты модели я воспользовался библиотекой gson.
Чтобы выгрузить фотографии книг, я воспользовался библиотекой Glide.
Чтобы понять как решить проблему с мерцаниями я перепробывал множество вариантов, было замечено, что на мгновение верхней карточке присваиваются значения нижней, из-за чего и получается этот неприятный эффект мерцания, но как избавиться от него на данный момент мне неизвестно (проблема решается если просто не показывать нижнюю карточку при свайпе (тоесть верхняя карточка просто свайпается без анимации того что под этой картой лежит нижняя которая выдвигается на ее место), но это не тот результат к которому нужно прийти).
