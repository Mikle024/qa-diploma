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
        PaymentCardPage.fillInTheForm(card,
                date,
                name,
                cvc);
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
        PaymentCardPage.fillInTheForm(card,
                date,
                name,
                cvc);
        PaymentCardPage.clickContinue();
        PaymentCardPage.checkNotificationRejectionContent();

        var expectedStatusSQL = card.getStatus();
        var actualStatusSQL = SQLHelper.getCardPaymentStatus();
        assertEquals(expectedStatusSQL, actualStatusSQL);
    }
}