package ru.netology.qa.data.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;

public class PaymentCreditPage {
    private SelenideElement header = $("[class='heading heading_size_m heading_theme_alfa-on-white']");

    public PaymentCreditPage() {
        header.shouldBe(visible);
    }
}
