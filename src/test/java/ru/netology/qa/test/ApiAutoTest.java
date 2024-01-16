package ru.netology.qa.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.qa.data.ApiHelper;
import ru.netology.qa.data.SQLHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.qa.data.SQLHelper.clearDB;

public class ApiAutoTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setup() {
        clearDB();
    }

    String pathForDebit = "/api/v1/pay";
    String pathForCredit = "/api/v1/credit";
    int successfulCode = 200;

    @Test
    @DisplayName("API, Позитивный сценарий покупки с оплатой по карте, статус карты APPROVED")
    void shouldReturn200CodeAndAnApprovedCardStatusDebit() {
        var card = ApiHelper.generateApiCardInfoApproved();

        var expectedStatusCard = card.getStatus();
        var actualStatusCard = ApiHelper.returnResponse(card, pathForDebit, successfulCode);
        assertEquals(expectedStatusCard, actualStatusCard);
    }

    @Test
    @DisplayName("API, Негативный сценарий покупки с оплатой по карте, статус карты DECLINED")
    void shouldReturn200CodeAndAnDeclinedCardStatusDebit() {
        var card = ApiHelper.generateApiCardInfoDeclined();

        var expectedStatusCard = card.getStatus();
        var actualStatusCard = ApiHelper.returnResponse(card, pathForDebit, successfulCode);
        assertEquals(expectedStatusCard, actualStatusCard);
    }

    @Test
    @DisplayName("API, Позитивный сценарий покупки с оплатой в кредит, статус карты APPROVED")
    void shouldReturn200CodeAndAnApprovedCardStatusCredit() {
        var card = ApiHelper.generateApiCardInfoApproved();

        var expectedStatusCard = card.getStatus();
        var actualStatusCard = ApiHelper.returnResponse(card, pathForCredit, successfulCode);
        assertEquals(expectedStatusCard, actualStatusCard);
    }

    @Test
    @DisplayName("API, Негативный сценарий покупки с оплатой в кредит, статус карты DECLINED")
    void shouldReturn200CodeAndAnDeclinedCardStatusCredit() {
        var card = ApiHelper.generateApiCardInfoDeclined();

        var expectedStatusCard = card.getStatus();
        var actualStatusCard = ApiHelper.returnResponse(card, pathForCredit, successfulCode);
        assertEquals(expectedStatusCard, actualStatusCard);
    }
}
