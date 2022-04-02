import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CreateOrderWithoutAuthorizationTest {
    String bearer = "";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Проверка создания заказа с индигриентами без авторизации")
    public void checkCreateOrderWithoutAuthorization() {

        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        Response response = sendPostCreateOrder(json, bearer);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Проверка создания заказа без индигриентов и без авторизации")
    public void checkCreateOrderWithoutAuthorizationAndWithoutIngredients() {
        String json = "{\"ingredients\": []}";
        Response response = sendPostCreateOrder(json, bearer);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("Проверка создания заказа с неправильными хешами индигриентов и без авторизации")
    public void checkCreateOrderWithoutAuthorizationAndWithBadHashIngredients() {
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaa23a6d\",\"61c0c5a71d1f82001b123daaa6f\"]}";
        Response response = sendPostCreateOrder(json, bearer);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(500);
    }

    @Step("Send POST request to api/orders")
    public Response sendPostCreateOrder(String json, String bearer) {
        Response response = given()
                .header("Authorization", bearer)
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("api/orders");
        return response;
    }

    @Step("Print response body to console")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
    }

}
