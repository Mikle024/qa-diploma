package ru.netology.qa.data.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement header = $("[class='heading heading_size_l heading_theme_alfa-on-white']");
    private static ElementsCollection buttons = $$("[class='button__content']");
    private static SelenideElement buttonPaymentCard = buttons.get(0);
    private static SelenideElement buttonPaymentCredit = buttons.get(1);

    public DashboardPage() {
        header.shouldBe(visible);
    }

    public static void selectPaymentCardPage() {
        buttonPaymentCard.click();
        new PaymentCardPage();
    }

    public static void selectPaymentCreditPage() {
        buttonPaymentCredit.click();
        new PaymentCreditPage();
    }
}
