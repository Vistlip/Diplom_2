import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class RefreshUserWithoutAuthorizationTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Проверка изменения логина пользователя без авторизации")
    public void checkUserCanChangeLoginWithoutAuthorization() {
        String json = "[ {\"op\": \"replace\", \"path\": \"/email\", \"value\": \"123456789@Gmail.com\"}]";
        Response response = refreshUser(json);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(401);
    }

    @Step("Send PATCH request to api/auth/user")
    public Response refreshUser(String json) {
        Response response = given()
                .body(json)
                .when()
                .patch("api/auth/user");
        return response;
    }

    @Step("Print response body to console")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
    }

}
