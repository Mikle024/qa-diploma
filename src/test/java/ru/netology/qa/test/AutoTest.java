package ru.netology.qa.test;

import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.qa.data.page.DashboardPage;
import ru.netology.qa.data.page.PaymentCardPage;

import static com.codeborne.selenide.Selenide.open;

public class AutoTest {

    @BeforeEach
    public void setup() {
        open("http://localhost:8080");
    }

    @DisplayName("Успешная покупка с оплатой по карте")
    @Test
    public void shouldSuccessfulPurchaseWithCardPayment() {
        DashboardPage.selectPaymentCardPage();
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
}