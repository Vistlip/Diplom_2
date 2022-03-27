import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class LoginUserTest {
    String jsonCreate;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void checkUserCanLogIn() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        Response response = sendPostLoginUser(jsonCreate);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(200);

    }

    @Test
    @DisplayName("Проверка авторизации пользователя c неправильным логином")
    public void checkUserIncorrectLogin() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        String jsonCreateIncorrectLogin = "{\"email\":\"" + "login333" + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        Response response = sendPostLoginUser(jsonCreateIncorrectLogin);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(401);

    }

    @Test
    @DisplayName("Проверка авторизации пользователя c неправильным паролем")
    public void checkUserIncorrectPass() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        String jsonCreateIncorrectLogin = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + "pass" + "\"}";
        System.out.println(jsonCreate);
        Response response = sendPostLoginUser(jsonCreateIncorrectLogin);
        printResponseBodyToConsole(response);
        response.then().assertThat().statusCode(401);

    }

    @Step("Send POST request to api/auth/login")
    public Response sendPostLoginUser(String json) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("api/auth/login");
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
