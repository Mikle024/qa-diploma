package ru.netology.qa.test;

import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.qa.data.page.DashboardPage;
import ru.netology.qa.data.page.PaymentCardPage;

import static com.codeborne.selenide.Selenide.open;

public class AutoTest {
    DashboardPage dashboardPage;

    @BeforeEach
    public void setup() {
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
    }
}