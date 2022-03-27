import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class CheckGetListTest {
    String jsonCreate;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Проверка получения списка заказов пользователя с авторизацией")
    public void checkGetListOrderWithAuthorization() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        String bearer = sendPostLoginUserAndGetBearer(jsonCreate);
        Response response = sendGetListOrder(bearer);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Проверка получения списка заказов пользователя без авторизации")
    public void checkGetListOrderWithoutAuthorization() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        String bearer = "";
        Response response = sendGetListOrder(bearer);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(401);
    }

    @Step("Send POST request to api/auth/login")
    public String sendPostLoginUserAndGetBearer(String json) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("api/auth/login");
        UserData userBearer = response.body().as(UserData.class);
        String bearer = userBearer.getAccessToken();
        return bearer;
    }

    @Step("Send get request to api/orders")
    public Response sendGetListOrder(String bearer) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", bearer)
                .when()
                .get("api/orders");
        return response;
    }

    @Step("Print response body to console")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
    }

    @After
    public void deleteCreateUser() {
        DeleteUser deleteUser = new DeleteUser();
        String answer = deleteUser.DeleteUser(jsonCreate);
        System.out.println(answer);
    }
}
