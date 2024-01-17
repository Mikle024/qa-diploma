[![Build status](https://ci.appveyor.com/api/projects/status/ys0xwogudyhrnado?svg=true)](https://ci.appveyor.com/project/Mikle024/qa-diploma)

# **Дипломный проект по профессии «Тестировщик ПО»**

Проект "Тестирование веб-сервиса" представляет собой автоматизированный комплексный тест,
охватывающий функциональность веб-сервиса взаимодействия с СУБД и API банка.
В рамках данного проекта тестируется процесс покупки тура по фиксированной цене, предоставляя
пользователям два варианта оплаты: стандартную оплату с использованием дебетовой карты и
возможность приобретения тура в кредит с использованием банковской карты.

**Документация**:
[План автоматизации тестирования веб-сервиса покупки тура.](https://github.com/Mikle024/qa-diploma/blob/main/documentation/Plan.md)
[Отчет по результатам тестирования](https://github.com/Mikle024/qa-diploma/blob/main/documentation/Report.md)
[Отчёт по итогам автоматизации](https://github.com/Mikle024/qa-diploma/blob/main/documentation/Summary.md)


_**Данный проект полностью настроен для работы с СУБД MySQL.**_
<details><summary>Для работы с PostgreSQL</summary>


    Во время запуска jar-файла и прогона тестов используйте флаг с указанием к базе данных:
    "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app"

</details>

Необходимое установленное ПО для запуска данного проекта на вашем ПК:

- [GIT](https://git-scm.com/) - система контроля версий
- [IDEA](https://www.jetbrains.com/ru-ru/idea/) - среда разработки
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) - система для развертки контейнеров

## **Процедура запуска автотестов**

**1. С помощью GIT необходимо склонировать репозиторий с проектом себе на локальный компьютер, командой:**

`git clone git@github.com:Mikle024/qa-diploma.git`

**2. Открыть папку с проектом в IDEA.**

**3. Ниже предоставлены команды для запусков автотестов и формирования отчетов с помощью терминала IDEA:**

**ВНИМАНИЕ: каждую команду нужно запускать в отдельном окне консоли, либо использовать соответствующие флаги, для
запуска в фоновом режиме.**

 - **3.1** Команда для скачивания и развертки контейнеров:

`docker compose up --build`


 - **3.2** Команда для запуска SUT файла:

`java -jar artifacts/aqa-shop.jar`
 <details><summary>Команда для запука SUT с PostgreSQL</summary>

  `java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar`

</details>


- **3.3** Команда для сборки проекта и прогона тестов:

`./gradlew clean test`

 <details><summary>Команда для сборки с PostgreSQL</summary>

  `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`

</details>


- **3.4** Команда для формирования отчетов и их открытия в браузере:

`./gradlew allureServe `

(Если браузер автоматически не открыл, то можно пройти по адресу, который будет указан в выводе консоли после выполнения
команды)
![Снимок экрана 2024-01-15 154805](https://github.com/Mikle024/qa-diploma/assets/142490585/147b32ca-375a-4ed2-8f63-8dc48121e6a4)


## 4. Завершение работы:

 - **4.1** Команды для остановки сервисов - Allure, SUT, Docker compose:

сочетание клавиш `Ctrl + C` для Windows

сочетание клавиш `Cmd + C` для macOS

 - **4.2** После остановки контейнера, удалить его можно с помощью команды:

`docker compose down`
