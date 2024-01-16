package ru.netology.qa.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import static io.restassured.RestAssured.given;
import static ru.netology.qa.data.DataHelper.*;

public class ApiHelper {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static String returnResponse(APICardInfo card, String path, int apiStatus) {
        Response response = given()
                .spec(requestSpec)
                .body(card)
                .when()
                .post(path)
                .then()
                .statusCode(apiStatus)
                .extract().response();
        return response.jsonPath().getString("status");
    }

    public static APICardInfo generateApiCardInfoApproved() {
        var card = generateValidCardNumberApproved();
        var date = generateValidDate();
        var name = generateValidName();
        var cvc = generateValidCvc();
        var status = card.getStatus();
        return new APICardInfo(card.getCardNumber(), date.getMonth(), date.getYear(), name, cvc, status);
    }

    public static APICardInfo generateApiCardInfoDeclined() {
        var card = generateValidCardNumberDeclined();
        var date = generateValidDate();
        var name = generateValidName();
        var cvc = generateValidCvc();
        var status = card.getStatus();
        return new APICardInfo(card.getCardNumber(), date.getMonth(), date.getYear(), name, cvc, status);
    }

    @Value
    public static class APICardInfo {
        String number;
        String year;
        String month;
        String holder;
        String cvc;
        String status;
    }
}

