package ru.netology.qa.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import ru.netology.qa.data.DataHelper;
import ru.netology.qa.data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.qa.page.DashboardPage;
import ru.netology.qa.page.PaymentCardPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.qa.data.SQLHelper.clearDB;

public class PaymentCardAutoTest {
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
        DashboardPage.selectPaymentCardPage();
    }

    @Test
    @DisplayName("Успешная покупка с оплатой по карте, валидные данные")
    void shouldSuccessfulPurchaseWithCardPayment() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkNotificationSuccessfullyContent();

        var expectedStatusSQL = card.getStatus();
        var actualStatusSQL = SQLHelper.getCardPaymentStatus();
        assertEquals(expectedStatusSQL, actualStatusSQL);
    }

    @Test
    @DisplayName("Отказ в покупке с оплатой по карте, карта отклонена")
    void shouldRejectionPurchaseWithCardPayment() {
        var card = DataHelper.generateValidCardNumberDeclined();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkNotificationRejectionContent();

        var expectedStatusSQL = card.getStatus();
        var actualStatusSQL = SQLHelper.getCardPaymentStatus();
        assertEquals(expectedStatusSQL, actualStatusSQL);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, с незаполненными полями")
    void negativePurchaseScenarioWithBlankFields() {
        PaymentCardPage.clickContinue();

        PaymentCardPage.checkRedNotificationRequiredField();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, пустое поле Номер карты")
    void negativePurchaseScenarioBlankCardNumberField() {
        var card = DataHelper.generateEmptyCardNumber();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный номер карты, менее 16 цифр")
    void negativePurchaseScenarioInvalidCardNumberLessThan16Digits() {
        var card = DataHelper.generateShortCardNumber();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный номер карты, состоящий из нулей")
    void negativePurchaseScenarioInvalidCardNumberConsistingOfZeros() {
        var card = DataHelper.generateCardNumberOfZeros();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный номер карты, состоящий из букв")
    void negativePurchaseScenarioInvalidCardNumberConsistingOfLetters() {
        var card = DataHelper.generateAlphabeticalCardNumber();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный номер карты, состоящий из спец. символов")
    void negativePurchaseScenarioInvalidCardNumberConsistingOfSpecialSymbols() {
        var card = DataHelper.generateCardNumberWithSpecialCharacters();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный номер карты, ввод пробела")
    void negativePurchaseScenarioInvalidCardNumberSpaceEntry() {
        var card = DataHelper.generateCardNumberWithSpace();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный месяц, менее 2 цифр")
    void negativePurchaseScenarioInvalidMonthLessThan2Digits() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateOneCharacterMonth();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный месяц, больше 12")
    void negativePurchaseScenarioInvalidMonthGreaterThanTwelfth() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthGreaterThanTwelfth();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongCardBeyondTheExpirationDate();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, пустое поле Месяц")
    void negativePurchaseScenarioBlankMonthField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateEmptyMonth();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный месяц, состоящий из букв")
    void negativePurchaseScenarioInvalidMonthConsistingOfLetters() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthOfAlphabetical();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный месяц, состоящий из спец. символов")
    void negativePurchaseScenarioInvalidMonthConsistingOfSpecialSymbols() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthOfSpecialCharacter();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный месяц, ввод пробела")
    void negativePurchaseScenarioInvalidMonthSpaceEntry() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateMonthOfSpace();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный год, истекший срок")
    void negativePurchaseScenarioInvalidYearExpiredTerm() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidFutureDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongCardBeyondTheExpirationDate();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, пустое поле Год")
    void negativePurchaseScenarioEmptyFieldYear() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateEmptyYear();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный год, менее 2 цифр")
    void negativePurchaseScenarioInvalidYearLessThan2Digits() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateOneCharacterYear();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, прошедшая дата")
    void negativePurchaseScenarioPastDate() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidPastDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongCardExpiration();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный год, состоящий из букв")
    void negativePurchaseScenarioEnterLettersInTheYearField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateYearOfAlphabetical();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный год, состоящий из спец. символов")
    void negativePurchaseScenarioInvalidYearConsistingOfSpecialSymbols() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateYearOfSpecialCharacter();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный год, ввод пробела")
    void negativePurchaseScenarioInvalidYearSpaceEntry() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateInvalidDateYearOfSpace();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Позитивный сценарий покупки, использование дефиса в имени")
    void positivePurchaseScenarioOfHyphenInAName() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidHyphenInAName();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkNotificationSuccessfullyContent();

        var expectedStatusSQL = card.getStatus();
        var actualStatusSQL = SQLHelper.getCardPaymentStatus();
        assertEquals(expectedStatusSQL, actualStatusSQL);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, пустое поле Владелец")
    void negativePurchaseScenarioBlankOwnerField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, "", cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидное имя владельца, состоящее из цифр")
    void negativePurchaseScenarioInvalidOwnerNameConsistingOfDigits() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidCvc();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидное имя владельца, состоящее из спец. символов")
    void negativePurchaseScenarioInvalidOwnerNameConsistingOfSpecialSymbols() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateRandomSpecialChar();
        var cvc = DataHelper.generateValidCvc();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, пустое поле CVC/CVV")
    void negativePurchaseScenarioEmptyCvcField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        PaymentCardPage.fillInTheForm(card, date, name, "");
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный CVC/CVV, состоящий из одной цифры")
    void negativePurchaseScenarioInvalidCvcConsistingOfOneDigit() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateRandomNumber();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный CVC/CVV, состоящий из букв")
    void negativePurchaseScenarioEnterLettersInCvcField() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateRandomWord();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный CVC/CVV, состоящий из спец. символов")
    void negativePurchaseScenarioInvalidCvcConsistingOfSpecialSymbols() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        var cvc = DataHelper.generateRandomSpecialChar();
        PaymentCardPage.fillInTheForm(card, date, name, cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, невалидный CVC/CVV, состоящий из пробела")
    void negativePurchaseScenarioInvalidCvcSpaceEntry() {
        var card = DataHelper.generateValidCardNumberApproved();
        var date = DataHelper.generateValidDate();
        var name = DataHelper.generateValidName();
        PaymentCardPage.fillInTheForm(card, date, name, " ");
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkRedNotificationWrongFormat();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }
}