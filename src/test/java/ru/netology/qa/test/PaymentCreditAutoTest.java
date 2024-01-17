package ru.netology.qa.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.qa.data.DataHelper;
import ru.netology.qa.data.SQLHelper;
import ru.netology.qa.page.DashboardPage;
import ru.netology.qa.page.PaymentCreditPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.qa.data.SQLHelper.clearDB;

public class PaymentCreditAutoTest {
    static {
        Configuration.headless = true;
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    DashboardPage dashboardPage;

    @BeforeEach
    public void setup() {
        clearDB();
        dashboardPage = open("http://localhost:8080", DashboardPage.class);
        DashboardPage.selectPaymentCreditPage();
    }

    @Test
    @DisplayName("Успешная покупка с оплатой в кредит, валидные данные")
    void shouldSuccessfulPurchaseWithCreditPayment() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkNotificationSuccessfullyContent();

        var expectedStatusSQL = card.getStatus();
        var actualStatusSQL = SQLHelper.getCreditPaymentStatus();
        assertEquals(expectedStatusSQL, actualStatusSQL);
    }

    @Test
    @DisplayName("Отказ в покупке с оплатой в кредит, карта отклонена")
    void shouldRejectionPurchaseWithCreditPayment() {
        var card = DataHelper.generateValidCardNumberDeclined();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkNotificationRejectionContent();

        var expectedStatusSQL = card.getStatus();
        var actualStatusSQL = SQLHelper.getCreditPaymentStatus();
        assertEquals(expectedStatusSQL, actualStatusSQL);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, с незаполненными полями")
    void negativePurchaseScenarioWithBlankFields() {
        PaymentCreditPage.clickContinue();

        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, пустое поле Номер карты")
    void negativePurchaseScenarioBlankCardNumberField() {
        var card = DataHelper.generateEmptyCardNumber();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный номер карты, менее 16 цифр")
    void negativePurchaseScenarioInvalidCardNumberLessThan16Digits() {
        var card = DataHelper.generateShortCardNumber();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный номер карты, состоящий из нулей")
    void negativePurchaseScenarioInvalidCardNumberConsistingOfZeros() {
        var card = DataHelper.generateCardNumberOfZeros();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный номер карты, состоящий из букв")
    void negativePurchaseScenarioInvalidCardNumberConsistingOfLetters() {
        var card = DataHelper.generateAlphabeticalCardNumber();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный номер карты, состоящий из спец. символов")
    void negativePurchaseScenarioInvalidCardNumberConsistingOfSpecialSymbols() {
        var card = DataHelper.generateCardNumberWithSpecialCharacters();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный номер карты, ввод пробела")
    void negativePurchaseScenarioInvalidCardNumberSpaceEntry() {
        var card = DataHelper.generateCardNumberWithSpace();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный месяц, менее 2 цифр")
    void negativePurchaseScenarioInvalidMonthLessThan2Digits() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateOneCharacterMonth();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки c оплатой в кредит, невалидный месяц, больше 12")
    void negativePurchaseScenarioInvalidMonthGreaterThanTwelfth() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthGreaterThanTwelfth();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongCardBeyondTheExpirationDate();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, пустое поле Месяц")
    void negativePurchaseScenarioBlankMonthField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateEmptyMonth();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный месяц, состоящий из нулей")
    void negativePurchaseScenarioInvalidMonthConsistingOfZeros() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthConsistingOfZeros();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный месяц, состоящий из букв")
    void negativePurchaseScenarioInvalidMonthConsistingOfLetters() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthOfAlphabetical();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный месяц, состоящий из спец. символов")
    void negativePurchaseScenarioInvalidMonthConsistingOfSpecialSymbols() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthOfSpecialCharacter();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный месяц, ввод пробела")
    void negativePurchaseScenarioInvalidMonthSpaceEntry() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthOfSpace();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный год, истекший срок")
    void negativePurchaseScenarioInvalidYearExpiredTerm() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidFutureDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongCardBeyondTheExpirationDate();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, пустое поле Год")
    void negativePurchaseScenarioEmptyFieldYear() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateEmptyYear();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный год, менее 2 цифр")
    void negativePurchaseScenarioInvalidYearLessThan2Digits() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateOneCharacterYear();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, прошедшая дата")
    void negativePurchaseScenarioPastDate() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidPastDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongCardExpiration();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный год, состоящий из нулей")
    void negativePurchaseScenarioInvalidYearConsistingOfZeros() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateYearConsistingOfZeros();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongCardExpiration();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный год, состоящий из букв")
    void negativePurchaseScenarioEnterLettersInTheYearField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateYearOfAlphabetical();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный год, состоящий из спец. символов")
    void negativePurchaseScenarioInvalidYearConsistingOfSpecialSymbols() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateYearOfSpecialCharacter();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный год, ввод пробела")
    void negativePurchaseScenarioInvalidYearSpaceEntry() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateYearOfSpace();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Позитивный сценарий покупки c оплатой в кредит, использование дефиса в имени")
    void positivePurchaseScenarioOfHyphenInAName() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidHyphenInAName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkNotificationSuccessfullyContent();

        var expectedStatusSQL = card.getStatus();
        var actualStatisSQL = SQLHelper.getCreditPaymentStatus();
        assertEquals(expectedStatusSQL, actualStatisSQL);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, пустое поле Владелец")
    void negativePurchaseScenarioBlankOwnerField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, "", cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидное имя владельца, состоящее из цифр")
    void negativePurchaseScenarioInvalidOwnerNameConsistingOfDigits() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidCvc();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидное имя владельца, состоящее из спец. символов")
    void negativePurchaseScenarioInvalidOwnerNameConsistingOfSpecialSymbols() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateRandomSpecialChar();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидное имя владельца, состоящее из кириллицы")
    void negativePurchaseScenarioInvalidOwnerNameConsistingOfCyrillicCharacters() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateInvalidNameConsistingOfCyrillicCharacters();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидное имя владельца, состоящее из более 50 символов")
    void negativePurchaseScenarioInvalidOwnerNameWithMoreThan50Characters() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateInvalidNameWithMoreThan50Characters();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидное имя владельца, состоящее из менее 2 символов")
    void negativePurchaseScenarioInvalidOwnerNameWithLessThan2Characters() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateInvalidNameWithLessThan2Characters();
        var cvc = DataHelper.generateValidCvc();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, пустое поле CVC/CVV")
    void negativePurchaseScenarioEmptyCvcField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        PaymentCreditPage.fillInTheForm(card, date, name, "");
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный CVC/CVV, состоящий из одной цифры")
    void negativePurchaseScenarioInvalidCvcConsistingOfOneDigit() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateRandomNumber();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный CVC/CVV, состоящий из букв")
    void negativePurchaseScenarioEnterLettersInCvcField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateRandomWord();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный CVC/CVV, состоящий из спец. символов")
    void negativePurchaseScenarioInvalidCvcConsistingOfSpecialSymbols() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateRandomSpecialChar();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный CVC/CVV, состоящий из пробела")
    void negativePurchaseScenarioInvalidCvcSpaceEntry() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        PaymentCreditPage.fillInTheForm(card, date, name, " ");
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkNotificationRejectionContent();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки с оплатой в кредит, невалидный CVC/CVV, состоящий из нулей")
    void negativePurchaseScenarioInvalidCvcOfZeros() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateInvalidCvcOfZeros();
        PaymentCreditPage.fillInTheForm(card, date, name, cvc);
        PaymentCreditPage.clickContinue();
        PaymentCreditPage.checkNotificationRejectionContent();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }
}
