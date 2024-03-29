# **План автоматизации тестирования веб-сервиса покупки тура.**

Документации валидных данных для карт нет.
- **Описание валидных данных для поля "Номер карты"** :
  - валидными данными считаются 16 цифр от 0 до 9, в формате 1234 5678 9012 3456.
  - В эмулятор банковского сервиса записаны предопределённые ответы для заданного набора карт:
    1. для карты "4444 4444 4444 4441" - **покупка одобрена**.
    1. для карты "4444 4444 4444 4442" - **покупка отклонена**.
- **Описание валидных данных для полей "Месяц" и "Год"** :
  - валидной датой считается текущий или будущий месяц и год, но не более 5 лет, в формате mm YY. Например, 07 и 24.
- **Описание валидных данных для поля "Владелец"** :
  - валидными данными считаются имена написанные на латинице заглавными или строчными буквами, от 2 до 50 символов, например, Aleksandr.
  - валидными данными считается наличие знака дефис «-», и наличие пробела например, Anna-Maria Popova.
- **Описание валидных данных для полей "CVC/CVV"** :
  - валидными данными считаются числа от 001 до 999. Например, 123.

## **1. Перечень автоматизируемых сценариев:**

### **Сценарии оплаты по карте** :

Предусловие для всех позитивных и негативных сценариев с оплатой по карте:

- открыть в браузере главную страницу сайта [http://localhost:8080/](http://localhost:8080/)
- нажать на кнопку «Купить»
- отображается вкладка «Оплата по карте».

- **Сценарий 1.1 "Позитивный сценарий покупки по карте"** :
  - заполнить поле «Номер карты» валидными данными, из записанного набора, 4444 4444 4444 4441
  - заполнить поля «Месяц», «Год», «Владелец», «CVC/CVV» валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: отображается сообщение "Операция одобрена Банком.". Покупка отображается в БД с одобренным статусом_.

- **Сценарий 1.2 "Негативный сценарий покупки по карте, отказ покупки"** :
  - заполнить поле «Номер карты» валидными данными, из записанного набора, 4444 4444 4444 4442
  - заполнить поля «Месяц», «Год», «Владелец», «CVC/CVV» валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: отображается сообщение "Ошибка! Банк отказал в проведении операции.". Покупка не отображается в БД_.

- **Сценарий 1.3 "Попытка покупки, с незаполненными полями"** :
  - оставить поля "Номер карты", "Месяц", "Год", "Владелец", «CVC/CVV» незаполненными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полями "Номер карты", "Месяц", "Год", "Владелец", «CVC/CVV» отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

#### **Проверка полей** :

**Номер карты**:
- **Сценарий 2.1 "Негативный сценарий покупки, пустое поле "Номер карты""** :
  - заполнить поля "Месяц", "Год", "Владелец", «CVC/CVV» валидными данными
  - оставить поле "Номер карты" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Номер карты" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 2.2 "Негативный сценарий покупки, невалидный номер карты, менее 16 цифр"** :
  - заполнить поле "Номер карты", цифрами менее 16 символов, например, 1234
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Номер карты" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 2.3 "Негативный сценарий покупки, невалидный номер карты, состоящий из нулей"** :
  - заполнить поле "Номер карты", нулями - 0000 0000 0000 0000
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Номер карты" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 2.4 "Негативный сценарий покупки, невалидный номер карты, состоящий из букв""** :
  - попытаться заполнить поле "Номер карты" буквенным значением, например, Alexandr
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Номер карты" останется пустым. Под полем "Номер карты" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 2.5 "Негативный сценарий покупки, невалидный номер карты, состоящий из спец. символов""** :
  - попытаться заполнить поле "Номер карты" спец. символами, например, ^#%&@
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Номер карты" останется пустым. Под полем "Номер карты" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 2.6 "Негативный сценарий покупки, невалидный номер карты, ввод пробела""** :
  - попытаться заполнить поле "Номер карты" пробелом
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Номер карты" останется пустым. Под полем "Номер карты" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

**Месяц**:
- **Сценарий 3.1 "Негативный сценарий покупки, невалидный месяц, больше 12"** :
  - заполнить поле "Месяц", цифрой больше 12, например, 13
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Месяц" отображается сообщение "Неверно указан срок действия карты". Покупка не отображается в БД_.

- **Сценарий 3.2 "Негативный сценарий покупки, невалидный месяц, менее 2 цифр"** :
  - заполнить поле "Месяц", одной цифрой, например, 1
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Месяц" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 3.3 "Негативный сценарий покупки, пустое поле "Месяц""** :
  - заполнить поля "Номер карты", "Год", "Владелец", «CVC/CVV» валидными данными
  - оставить поле "Месяц" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Месяц" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 3.4 "Негативный сценарий покупки, невалидный месяц, состоящий из нулей""** :
  - заполнить поле "Месяц", нулями - 00
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Месяц" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 3.5 "Негативный сценарий покупки, невалидный месяц, состоящий из букв""** :
  - попытаться заполнить поле "Месяц" буквенным значением, например, AX
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Месяц" останется пустым. Под полем "Месяц" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 3.6 "Негативный сценарий покупки, невалидный месяц, состоящий из спец. символов"** :
  - попытаться заполнить поле "Месяц" спец. символами, например, ^#%&@
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Месяц" останется пустым. Под полем "Месяц" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 3.7 "Негативный сценарий покупки, невалидный месяц, ввод пробела"** :
  - попытаться заполнить поле "Месяц" пробелом
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Месяц" останется пустым. Под полем "Месяц" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

**Год**:
- **Сценарий 4.1 "Негативный сценарий покупки, невалидный год, истекший срок"** :
  - заполнить поле "Год", на значение более 6 лет от текущий даты, например, если сейчас 24, то 30
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Неверно указан срок действия карты". Покупка не отображается в БД_.

- **Сценарий 4.2 "Негативный сценарий покупки, пустое поле "Год""** :
  - заполнить поля "Номер карты", "Месяц", "Владелец", «CVC/CVV» валидными данными
  - оставить поле "Год" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 4.3 "Негативный сценарий покупки, невалидный год, менее 2 цифр"** :
  - заполнить поле "Год", одной цифрой, например, 1
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 4.4 "Негативный сценарий покупки, прошедшая дата""** :
  - заполнить поля "Месяц" и "Год", данными прошлого месяца. Например 01/24, при условии, если сейчас 02/24
  - заполнить поля "Номер карты", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Истёк срок действия карты". Покупка не отображается в БД_.

- **Сценарий 4.5 "Негативный сценарий покупки, невалидный год, состоящий из нулей""** :
  - заполнить поле "Год", нулями - 00
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 4.6 "Негативный сценарий покупки, невалидный год, состоящий из букв""** :
  - попытаться заполнить поле "Год" буквенным значением, например, AX
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Год" останется пустым. Под полем "Год" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 4.7 "Негативный сценарий покупки, невалидный год, состоящий из спец. символов"** :
  - попытаться заполнить поле "Год" спец. символами, например, ^#%&
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Год" останется пустым. Под полем "Год" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 4.8 "Негативный сценарий покупки, невалидный год, ввод пробела"** :
  - попытаться заполнить поле "Год" пробелом
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
_ожидаемый результат: поле "Год" останется пустым. Под полем "Год" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

**Владелец**:
- **Сценарий 5.1 "Позитивный сценарий покупки, использование символа "-" в имени"** :
  - заполнить поле «Владелец» валидными данными с использованием дефиса, например, Anna-Maria Popova
  - заполнить поле «Номер карты» валидными данными, из записанного набора, 4444 4444 4444 4441
  - заполнить поля «Месяц», «Год», «CVC/CVV» валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: отображается сообщение "Операция одобрена Банком.". Покупка отображается в БД с одобренным статусом_.

- **Сценарий 5.2 "Негативный сценарий покупки, пустое поле "Владелец""** :
  - заполнить поля "Номер карты", "Месяц", "Год", «CVC/CVV» валидными данными
  - оставить поле "Владелец" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 5.3 "Негативный сценарий покупки, невалидное имя владельца, состоящее из цифр"** :
  - заполнить поле "Владелец", числовым значением, например, 1234
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 5.4 "Негативный сценарий покупки, невалидное имя владельца, состоящее из спец. символов"** :
  - заполнить поле "Владелец", спец. символами, например, ^#%&@
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 5.5 "Негативный сценарий покупки, невалидное имя владельца, состоящее из кириллицы"** :
  - заполнить поле "Владелец", кириллицей, например, Александр Иванов
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 5.6 "Негативный сценарий покупки, невалидное имя владельца, состоящее из более 50 символов"** :
  - заполнить поле "Владелец", латиницей, более 51 символов
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 5.7 "Негативный сценарий покупки, невалидное имя владельца, состоящее из менее 2 символов"** :
  - заполнить поле "Владелец", одним символом латиницы, например, J
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

**CVC/CVV**:
- **Сценарий 6.1 "Негативный сценарий покупки, пустое поле "CVC/CVV""** :
  - заполнить поля "Номер карты", "Месяц", "Год", «Владелец» валидными данными
  - оставить поле "CVC/CVV" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "CVC/CVV" отображается сообщение "Поле обязательно для заполнения ". Покупка не отображается в БД_.

- **Сценарий 6.2 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из одной цифры"** :
  - заполнить поле "CVC/CVV", одной цифрой, например, 1
  - заполнить поля "Номер карты", "Месяц", "Год", «Владелец» валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "CVC/CVV" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 6.3 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из букв"** :
  - попытаться заполнить поле "CVC/CVV" буквенным значением, например, Ale
  - заполнить поля "Номер карты", "Месяц", «Год», "Владелец" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: поле "CVC/CVV" останется пустым. Под полем "CVC/CVV" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 6.4 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из спец. символов"** :
  - заполнить поле "CVC/CVV" спец. символами, например, ^#%&@
  - заполнить поля "Номер карты", "Месяц", «Год», "Владелец" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "CVC/CVV" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 6.5 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из пробела"** :
  - попытаться заполнить поле "CVC/CVV" пробелом
  - заполнить поля "Номер карты", "Месяц", «Год», "Владелец" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: поле "CVC/CVV" останется пустым. Под полем "CVC/CVV" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 6.6 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из нулей"** :
  - попытаться заполнить поле "CVC/CVV" нулями - 000
  - заполнить поля "Номер карты", "Месяц", «Год», "Владелец" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "CVC/CVV" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

### **Сценарии оплаты в кредит** :

Предусловие для всех позитивных и негативных сценариев с оплатой в кредит:

- открыть в браузере главную страницу сайта [http://localhost:8080/](http://localhost:8080/)
- нажать на кнопку «Купить в кредит»
- отображается вкладка «Кредит по данным карты».

- **Сценарий 7.1 "Позитивный сценарий покупки в кредит"** :
  - заполнить поле «Номер карты» валидными данными, из записанного набора, 4444 4444 4444 4441
  - заполнить поля «Месяц», «Год», «Владелец», «CVC/CVV» валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: отображается сообщение "Операция одобрена Банком.". Покупка отображается в БД с одобренным статусом_.

- **Сценарий 7.2 "Негативный сценарий покупки в кредит, отказ покупки"** :
  - заполнить поле «Номер карты» валидными данными, из записанного набора, 4444 4444 4444 4442
  - заполнить поля «Месяц», «Год», «Владелец», «CVC/CVV» валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: отображается сообщение "Ошибка! Банк отказал в проведении операции.". Покупка не отображается в БД_.

- **Сценарий 7.3 "Попытка покупки, с незаполненными полями"** :
  - оставить поля "Номер карты", "Месяц", "Год", "Владелец", «CVC/CVV» незаполненными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полями "Номер карты", "Месяц", "Год", "Владелец", «CVC/CVV» отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

#### **Проверка полей** :

**Номер карты**:
- **Сценарий 8.1 "Негативный сценарий покупки, пустое поле "Номер карты""** :
  - заполнить поля "Месяц", "Год", "Владелец", «CVC/CVV» валидными данными
  - оставить поле "Номер карты" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Номер карты" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 8.2 "Негативный сценарий покупки, невалидный номер карты, менее 16 цифр"** :
  - заполнить поле "Номер карты", цифрами менее 16 символов, например, 1234
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Номер карты" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 8.3 "Негативный сценарий покупки, невалидный номер карты, состоящий из нулей"** :
  - заполнить поле "Номер карты", нулями - 0000 0000 0000 0000
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Номер карты" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 8.4 "Негативный сценарий покупки, невалидный номер карты, состоящий из букв""** :
  - попытаться заполнить поле "Номер карты" буквенным значением, например, Alexandr
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Номер карты" останется пустым. Под полем "Номер карты" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 8.5 "Негативный сценарий покупки, невалидный номер карты, состоящий из спец. символов""** :
  - попытаться заполнить поле "Номер карты" спец. символами, например, ^#%&@
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Номер карты" останется пустым. Под полем "Номер карты" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 8.6 "Негативный сценарий покупки, невалидный номер карты, ввод пробела""** :
  - попытаться заполнить поле "Номер карты" пробелом
  - заполнить поля "Месяц", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Номер карты" останется пустым. Под полем "Номер карты" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

**Месяц**:
- **Сценарий 9.1 "Негативный сценарий покупки, невалидный месяц, больше 12"** :
  - заполнить поле "Месяц", цифрой больше 12, например, 13
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Месяц" отображается сообщение "Неверно указан срок действия карты". Покупка не отображается в БД_.

- **Сценарий 9.2 "Негативный сценарий покупки, невалидный месяц, менее 2 цифр"** :
  - заполнить поле "Месяц", одной цифрой, например, 1
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Месяц" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 9.3 "Негативный сценарий покупки, пустое поле "Месяц""** :
  - заполнить поля "Номер карты", "Год", "Владелец", «CVC/CVV» валидными данными
  - оставить поле "Месяц" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Месяц" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 9.4 "Негативный сценарий покупки, невалидный месяц, состоящий из нулей""** :
  - заполнить поле "Месяц", нулями - 00
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Месяц" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 9.5 "Негативный сценарий покупки, невалидный месяц, состоящий из букв""** :
  - попытаться заполнить поле "Месяц" буквенным значением, например, AX
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Месяц" останется пустым. Под полем "Месяц" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 9.6 "Негативный сценарий покупки, невалидный месяц, состоящий из спец. символов"** :
  - попытаться заполнить поле "Месяц" спец. символами, например, ^#%&@
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Месяц" останется пустым. Под полем "Месяц" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 9.7 "Негативный сценарий покупки, невалидный месяц, ввод пробела"** :
  - попытаться заполнить поле "Месяц" пробелом
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Месяц" останется пустым. Под полем "Месяц" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

**Год**:
- **Сценарий 10.1 "Негативный сценарий покупки, невалидный год, истекший срок"** :
  - заполнить поле "Год", на значение более 6 лет от текущий даты, например, если сейчас 24, то 30
  - заполнить поля "Номер карты", "Год", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Неверно указан срок действия карты". Покупка не отображается в БД_.

- **Сценарий 10.2 "Негативный сценарий покупки, пустое поле "Год""** :
  - заполнить поля "Номер карты", "Месяц", "Владелец", «CVC/CVV» валидными данными
  - оставить поле "Год" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 10.3 "Негативный сценарий покупки, невалидный год, менее 2 цифр"** :
  - заполнить поле "Год", одной цифрой, например, 1
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 10.4 "Негативный сценарий покупки, прошедшая дата""** :
  - заполнить поля "Месяц" и "Год", данными прошлого месяца. Например 01/24, при условии, если сейчас 02/24
  - заполнить поля "Номер карты", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Истёк срок действия карты". Покупка не отображается в БД_.

- **Сценарий 10.5 "Негативный сценарий покупки, невалидный год, состоящий из нулей""** :
  - заполнить поле "Год", нулями - 00
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Год" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 10.6 "Негативный сценарий покупки, невалидный год, состоящий из букв""** :
  - попытаться заполнить поле "Год" буквенным значением, например, AX
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Год" останется пустым. Под полем "Год" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 10.7 "Негативный сценарий покупки, невалидный год, состоящий из спец. символов"** :
  - попытаться заполнить поле "Год" спец. символами, например, ^#%&
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Год" останется пустым. Под полем "Год" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 10.8 "Негативный сценарий покупки, невалидный год, ввод пробела"** :
  - попытаться заполнить поле "Год" пробелом
  - заполнить поля "Номер карты", "Месяц", «Владелец», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"
    _ожидаемый результат: поле "Год" останется пустым. Под полем "Год" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

**Владелец**:
- **Сценарий 11.1 "Позитивный сценарий покупки, использование символа "-" в имени"** :
  - заполнить поле «Владелец» валидными данными с использованием дефиса, например, Anna-Maria Popova
  - заполнить поле «Номер карты» валидными данными, из записанного набора, 4444 4444 4444 4441
  - заполнить поля «Месяц», «Год», «CVC/CVV» валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: отображается сообщение "Операция одобрена Банком.". Покупка отображается в БД с одобренным статусом_.

- **Сценарий 11.2 "Негативный сценарий покупки, пустое поле "Владелец""** :
  - заполнить поля "Номер карты", "Месяц", "Год", «CVC/CVV» валидными данными
  - оставить поле "Владелец" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 11.3 "Негативный сценарий покупки, невалидное имя владельца, состоящее из цифр"** :
  - заполнить поле "Владелец", числовым значением, например, 1234
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 11.4 "Негативный сценарий покупки, невалидное имя владельца, состоящее из спец. символов"** :
  - заполнить поле "Владелец", спец. символами, например, ^#%&@
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 11.5 "Негативный сценарий покупки, невалидное имя владельца, состоящее из кириллицы"** :
  - заполнить поле "Владелец", кириллицей, например, Александр Иванов
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 11.6 "Негативный сценарий покупки, невалидное имя владельца, состоящее из более 50 символов"** :
  - заполнить поле "Владелец", латиницей, более 51 символов
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 11.7 "Негативный сценарий покупки, невалидное имя владельца, состоящее из менее 2 символов"** :
  - заполнить поле "Владелец", одним символом латиницы, например, J
  - заполнить поля "Номер карты", "Месяц", «Год», "CVC/CVV" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "Владелец" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

**CVC/CVV**:
- **Сценарий 12.1 "Негативный сценарий покупки, пустое поле "CVC/CVV""** :
  - заполнить поля "Номер карты", "Месяц", "Год", «Владелец» валидными данными
  - оставить поле "CVC/CVV" незаполненным
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "CVC/CVV" отображается сообщение "Поле обязательно для заполнения ". Покупка не отображается в БД_.

- **Сценарий 12.2 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из одной цифры"** :
  - заполнить поле "CVC/CVV", одной цифрой, например, 1
  - заполнить поля "Номер карты", "Месяц", "Год", «Владелец» валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "CVC/CVV" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 12.3 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из букв"** :
  - попытаться заполнить поле "CVC/CVV" буквенным значением, например, Ale
  - заполнить поля "Номер карты", "Месяц", «Год», "Владелец" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: поле "CVC/CVV" останется пустым. Под полем "CVC/CVV" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 12.4 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из спец. символов"** :
  - заполнить поле "CVC/CVV" спец. символами, например, ^#%&@
  - заполнить поля "Номер карты", "Месяц", «Год», "Владелец" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "CVC/CVV" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

- **Сценарий 12.5 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из пробела"** :
  - попытаться заполнить поле "CVC/CVV" пробелом
  - заполнить поля "Номер карты", "Месяц", «Год», "Владелец" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: поле "CVC/CVV" останется пустым. Под полем "CVC/CVV" отображается сообщение "Поле обязательно для заполнения". Покупка не отображается в БД_.

- **Сценарий 12.6 "Негативный сценарий покупки, невалидный CVC/CVV, состоящий из нулей"** :
  - попытаться заполнить поле "CVC/CVV" нулями - 000
  - заполнить поля "Номер карты", "Месяц", «Год», "Владелец" валидными данными
  - нажать кнопку "Продолжить"

_ожидаемый результат: под полем "CVC/CVV" отображается сообщение "Неверный формат". Покупка не отображается в БД_.

### **Сценарии тестирования API** :

- **Сценарий 13.1 "Позитивный сценарий покупки с оплатой по карте, статус карты APPROVED"** :
  - отправить POST запрос, в теле которого находятся данные валидной карты, из записанного набора, 4444 4444 4444 4441
  по пути http://localhost:8080/api/v1/pay

_ожидаемый результат: получен ответ с кодом 200, в теле ответа указан статус карты "APPROVED"_.

- **Сценарий 13.2 "Негативный сценарий покупки с оплатой по карте, статус карты DECLINED"** :
  - отправить POST запрос, в теле которого находятся данные валидной карты, из записанного набора, 4444 4444 4444 4442
    по пути http://localhost:8080/api/v1/pay

_ожидаемый результат: получен ответ с кодом 200, в теле ответа указан статус карты "DECLINED"_.

- **Сценарий 13.3 "Позитивный сценарий покупки с оплатой в кредит, статус карты APPROVED"** :
  - отправить POST запрос, в теле которого находятся данные валидной карты, из записанного набора, 4444 4444 4444 4441
    по пути http://localhost:8080/api/v1/credit

_ожидаемый результат: получен ответ с кодом 200, в теле ответа указан статус карты "APPROVED"_.

- **Сценарий 13.4 "Негативный сценарий покупки с оплатой в кредит, статус карты DECLINED"** :
  - отправить POST запрос, в теле которого находятся данные валидной карты, из записанного набора, 4444 4444 4444 4442
    по пути http://localhost:8080/api/v1/credit

_ожидаемый результат: получен ответ с кодом 200, в теле ответа указан статус карты "DECLINED"_.

_**Всего сценариев: 78. **_

## **2. Перечень используемых инструментов с обоснованием выбора:**

- IntelliJ IDEA - среда разработки для Java.
- Язык программирования: Java, благодаря простоте и широкой поддержке в сфере автоматизации тестирования.
- Git - система контроля версий проекта.
- GitHub - хранение репозиториев с кодом автотестов и баг-репортов.
- Lombok - аннотация, которая позволяет генерировать код.
- Инструмент автоматизации тестирования: Selenide, так как он поддерживает различные браузеры и является мощным инструментом для веб-автоматизации.
- Фреймворк для тестирования: JUnit 5 для удобного написания и запуска тестов.
- Gradle - инструмент автоматизации сборки и управления зависимостями.
- Docker - система контейнеризации.
- Docker Compose для запуска мультиконтейнерных приложений.
- REST Assured для взаимодействия с SQL базами данных.
- Allure - фреймворк для репортинга.

## **3. Перечень необходимых разрешений, данных и доступов:**

- Документация
- Валидные данные карт.
- Разрешение на использование автоматизированных средств для тестирования, от владельца SUT.
- Доступ к тестовой среде для проведения автоматизированных тестов.
- Доступа к БД, API.

## **4. Перечень и описание возможных рисков при автоматизации:**

- Обнаружение неработающего функционала.
- Изменение структуры страницы: если структура страницы изменится, может потребоваться пересмотреть и изменить локаторы элементов.
- Нестабильность среды: нестабильные соединения или серверные проблемы могут привести к неудачным тестам.
- Риск блокировки: использование бот-подобного поведения может привести к блокировке аккаунта на сайте.

## **5. Перечень необходимых специалистов для автоматизации:**

- QA-инженер с опытом автоматизации тестирования веб-приложений.

## **6. Интервальная оценка с учётом рисков в часах:**

- Планирование автоматизации тестирования: 24 часа.
- Запуск приложения и настройка окружения: 12 часа.
- Написание автотестов с необходимыми сценариями, тестирование и отладка: 168 часов.
- Формирование и подготовка отчетов: 48 часов.
- Резерв на риски и возможные изменения: 48 часов.

_Общая интервальная оценка: 300 часов (с учетом рисков и возможных изменений)_.

## **7. План сдачи работ:**

- Запуск приложения и настройка окружения - до 11.01.2024.
- Планирование автоматизации тестирования - до 13.01.2024.
- Написание автотестов, тестирование и отладка - до 18.01.2024.
- Формирование и подготовка отчетов - до 20.01.2024.
