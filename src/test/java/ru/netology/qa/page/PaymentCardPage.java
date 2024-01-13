package ru.netology.qa.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.qa.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentCardPage {
    private SelenideElement header = $("[class='heading heading_size_m heading_theme_alfa-on-white']");

    private static ElementsCollection inputs = $$("[class='input__control']");
    private static SelenideElement inputCardNumber = inputs.get(0);
    private static SelenideElement inputMonth = inputs.get(1);
    private static SelenideElement inputYear = inputs.get(2);
    private static SelenideElement inputName = inputs.get(3);
    private static SelenideElement inputCVC = inputs.get(4);

    private static SelenideElement buttonContinue = $$("[class='button__content']").get(2);

    private static SelenideElement redNotificationWrongFormat = $(byText("Неверный формат"));
    private static SelenideElement redNotificationRequiredField = $(byText("Поле обязательно для заполнения"));
    private static SelenideElement redNotificationWrongCardExpiration = $(byText("Истёк срок действия карты"));
    private static SelenideElement redNotificationWrongCardBeyondTheExpirationDate = $(byText("Неверно указан срок действия карты"));


    private static ElementsCollection notificationContent = $$(".notification__content");
    private static SelenideElement notificationSuccessfullyContent = notificationContent.get(0);
    private static SelenideElement notificationRejectionContent = notificationContent.get(1);

    public PaymentCardPage() {
        header.shouldBe(visible);
    }

    public static void fillInTheForm(DataHelper.CardNumber card,
                                     DataHelper.Date date,
                                     String setName,
                                     String setCVC) {
        inputCardNumber.setValue(card.getCardNumber());
        inputMonth.setValue(date.getMonth());
        inputYear.setValue(date.getYear());
        inputName.setValue(setName);
        inputCVC.setValue(setCVC);
    }

    public static void clickContinue() {
        buttonContinue.click();
    }

    public static void checkNotificationSuccessfullyContent() {
        notificationSuccessfullyContent
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Операция одобрена Банком."));
    }

    public static void checkNotificationRejectionContent() {
        notificationRejectionContent
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."));
    }

    public static void checkRedNotificationWrongFormat() {
        redNotificationWrongFormat
                .shouldBe(visible);
    }

    public static void checkRedNotificationRequiredField() {
        redNotificationRequiredField
                .shouldBe(visible);
    }

    public static void checkRedNotificationWrongCardExpiration() {
        redNotificationWrongCardExpiration
                .shouldBe(visible);
    }

    public static void checkRedNotificationWrongCardBeyondTheExpirationDate() {
        redNotificationWrongCardBeyondTheExpirationDate
                .shouldBe(visible);
    }
}
