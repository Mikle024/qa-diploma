package ru.netology.qa.data.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentCardPage {
    private SelenideElement header = $("[class='heading heading_size_m heading_theme_alfa-on-white']");
    private static ElementsCollection inputs = $$("[class='input__control']");
    private static SelenideElement cardNumber = inputs.get(0);
    private static SelenideElement month = inputs.get(1);
    private static SelenideElement year = inputs.get(2);
    private static SelenideElement name = inputs.get(3);
    private static SelenideElement cvc = inputs.get(4);
    private static ElementsCollection buttons = $$("[class='button__content']");
    private static SelenideElement buttonNext = buttons.get(2);

    public PaymentCardPage() {
        header.shouldBe(visible);
    }

    public static void fillInTheForm(DataHelper.CardNumber card,
                                     String setMonth,
                                     String setYears,
                                     String setName,
                                     String setCVC) {
        cardNumber.setValue(card.getCardNumber());
        month.setValue(setMonth);
        year.setValue(setYears);
        name.setValue(setName);
        cvc.setValue(setCVC);
    }

    public static void clickNext() {
        buttonNext.click();
    }
}
