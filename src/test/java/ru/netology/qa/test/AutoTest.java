package ru.netology.qa.test;

import com.codeborne.selenide.Condition;
import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.qa.data.page.DashboardPage;
import ru.netology.qa.data.page.PaymentCardPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
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
        var card = DataHelper.setValidCardNumberApproved();
        var month = DataHelper.generateMonth(0);
        var year = DataHelper.generateYear(0);
        var name = DataHelper.generateName("en");
        var cvc = DataHelper.generateCvc();
        PaymentCardPage.fillInTheForm(card,
                month,
                year,
                name,
                cvc);
        PaymentCardPage.clickNext();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text("Операция одобрена Банком."));
    }
}