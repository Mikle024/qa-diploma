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

public class AutoTest {
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
    @DisplayName("Попытка покупки, с незаполненными полями")
    void purchaseAttemptWithBlankFields() {
        PaymentCardPage.clickContinue();

        PaymentCardPage.checkRedNotificationWrongFormat();
        PaymentCardPage.checkRedNotificationRequiredField();

        var expectedEmptyDB = true;
        var actualEmptyDB = SQLHelper.checkEmptyDB();
        assertEquals(expectedEmptyDB, actualEmptyDB);
    }

    @Test
    @DisplayName("Негативный сценарий покупки, пустое поле \"Номер карты")
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
    @DisplayName("Попытка ввода букв в поле \"Номер карты")
    void attemptingToEnterLettersInTheCardNumberField() {
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
    @DisplayName("Попытка ввода спец. символов в поле \"Номер карты")
    void attemptingToEnterSpecialCharactersInTheCardNumberField() {
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
    @DisplayName("Попытка ввода пробела в поле \"Номер карты")
    void attemptingToEnterSpaceInTheCardNumberField() {
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
}