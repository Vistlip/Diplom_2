import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    String jsonCreate;
    @Before
    public void setUp() {
        RestAssured.baseURI= "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkCreateOrderWithAuthorizationAndIngredients() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        String bearer = sendPostLoginUserAndGetBearer(jsonCreate);
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        Response response = sendPostCreateOrder(json, bearer);
        compareUserStatusCode(response, 200);
        printResponseBodyToConsole(response);
        DeleteUser deleteUser = new DeleteUser();
        String answer = deleteUser.DeleteUser(jsonCreate);
        System.out.println(answer);
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkCreateOrderWithAuthorizationAndWithoutIngredients() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        String bearer = sendPostLoginUserAndGetBearer(jsonCreate);
        String json = "{\"ingredients\": []}";
        Response response = sendPostCreateOrder(json, bearer);
        compareUserStatusCode(response, 400);
        printResponseBodyToConsole(response);
        DeleteUser deleteUser = new DeleteUser();
        String answer = deleteUser.DeleteUser(jsonCreate);
        System.out.println(answer);
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkCreateOrderWithAuthorizationAndWithBadHashIngredients() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        String bearer = sendPostLoginUserAndGetBearer(jsonCreate);
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaa23a6d\",\"61c0c5a71d1f82001b123daaa6f\"]}";
        Response response = sendPostCreateOrder(json, bearer);
        compareUserStatusCode(response, 500);
        printResponseBodyToConsole(response);
        DeleteUser deleteUser = new DeleteUser();
        String answer = deleteUser.DeleteUser(jsonCreate);
        System.out.println(answer);
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkCreateOrderWithoutAuthorization() {
        String bearer = "";
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        Response response = sendPostCreateOrder(json, bearer);
        compareUserStatusCode(response, 200);
        printResponseBodyToConsole(response);
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkCreateOrderWithoutAuthorizationAndWithoutIngredients() {
        String bearer = "";
        String json = "{\"ingredients\": []}";
        Response response = sendPostCreateOrder(json, bearer);
        compareUserStatusCode(response, 400);
        printResponseBodyToConsole(response);
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkCreateOrderWithoutAuthorizationAndWithBadHashIngredients() {
        String bearer = "";
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaa23a6d\",\"61c0c5a71d1f82001b123daaa6f\"]}";
        Response response = sendPostCreateOrder(json, bearer);
        compareUserStatusCode(response, 500);
        printResponseBodyToConsole(response);
    }

    @Step("Send POST request to api/auth/login")
    public String sendPostLoginUserAndGetBearer (String json){
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("api/auth/login");
        UserData userBearer = response.body().as(UserData.class);
        String bearer = userBearer.getAccessToken();
        return bearer;
    }

    @Step("Send POST request to api/orders")
    public Response sendPostCreateOrder(String json, String bearer){
        Response response = given()
                .header("Authorization", bearer)
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("api/orders");
        return response;
    }

    @Step("Compare statusCode and body")
    public void compareUserStatusCodeAndMessage(Response response, int statusCode, String body){
        response.then().assertThat().body("user", equalTo(body)).statusCode(statusCode);
    }
    @Step("Print response body to console")
    public void printResponseBodyToConsole(Response response){
        System.out.println(response.body().asString());
    }

    @Step("Compare statusCode")
    public void compareUserStatusCode(Response response, int statusCode){
        response.then().assertThat().statusCode(statusCode);
    }

    @After
    public void deleteCreateUser() {

        DeleteUser deleteUser = new DeleteUser();
        String answer = deleteUser.DeleteUser(jsonCreate);
        System.out.println(answer);
    }
}

