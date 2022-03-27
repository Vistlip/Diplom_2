import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName; // импорт DisplayName
import io.qameta.allure.Step; // импорт Step

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    String jsonCreate;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Проверка - пользоваетля можно создать и успешный запрос возращает: success: true")
    public void checkCreateUser() {
        CreateNewLoginPass createNewLoginPass = new CreateNewLoginPass();
        jsonCreate = createNewLoginPass.registerNewLoginPass();
        System.out.println(jsonCreate);
        Response response = sendPostCreateUser(jsonCreate);
        printResponseBodyToConsole(response);
        response.then().assertThat().body("success", equalTo(true)).statusCode(200);
    }

    @Test
    @DisplayName("Проверка создания двух одинаковых пользователей")
    public void checkCreateAnAlreadyRegisteredUser() {
        BurgerRegisterUser burgerRegisterCourier = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterCourier.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String name = Arr.get(2);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        String jsonWithSameLogin = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + "pass" + "\","
                + "\"name\":\"" + name + "\"}";
        Response response = sendPostCreateUser(jsonWithSameLogin);
        printResponseBodyToConsole(response);
        response.then().assertThat().body("message", equalTo("User already exists")).statusCode(403);
    }

    @Test
    @DisplayName("Проверка создание пользователя без логина")
    public void checkCreatingUserWithoutLogin() {
        BurgerRegisterUser burgerRegisterCourier = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterCourier.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String name = Arr.get(2);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        String jsonWithoutLogin = "{\"password\":\"" + "pass" + "\","
                + "\"name\":\"" + name + "\"}";
        Response response = sendPostCreateUser(jsonWithoutLogin);
        printResponseBodyToConsole(response);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields")).statusCode(403);
    }

    @Test
    @DisplayName("Проверка создание пользователя без пароля")
    public void checkCreatingUserWithoutPass() {
        BurgerRegisterUser burgerRegisterCourier = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterCourier.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String name = Arr.get(2);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        String jsonWithoutPass = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"name\":\"" + name + "\"}";
        Response response = sendPostCreateUser(jsonWithoutPass);
        printResponseBodyToConsole(response);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields")).statusCode(403);
    }

    @Test
    @DisplayName("Проверка создание пользователя без имени")
    public void checkCreatingUserWithoutName() {
        BurgerRegisterUser burgerRegisterCourier = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterCourier.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String name = Arr.get(2);
        jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        String getJsonWithoutName = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + "pass" + "\"}";
        Response response = sendPostCreateUser(getJsonWithoutName);

        printResponseBodyToConsole(response);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields")).statusCode(403);
    }

    @Step("Send POST request to api/auth/register")
    public Response sendPostCreateUser(String body) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("api/auth/register");
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
