import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.*;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RefreshUserTest {
    String jsonCreate;

    @Before
    public void setUp() {
        RestAssured.baseURI= "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkUserCanRename() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        String bearer = sendPostLoginUserAndGetBearer(jsonCreate);
        String json = "[ {\"op\": \"replace\", \"path\": \"/name\", \"value\": \"Viz\"}]";
        Response response = refreshUser(bearer, json);
        String body = "<{email=" + login + "@yandex.ru, name=Viz>";
        compareUserStatusCodeAndMessage(response, 200, body);
        printResponseBodyToConsole(response);
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkUserCanChangeLogin() {
        BurgerRegisterUser burgerRegisterUser = new BurgerRegisterUser();
        ArrayList<String> Arr = burgerRegisterUser.registerNewUserAndReturnLoginPassword();
        String login = Arr.get(0);
        String pass = Arr.get(1);
        String name = Arr.get(2);
        String jsonCreate = "{\"email\":\"" + login + "@yandex.ru" + "\","
                + "\"password\":\"" + pass + "\"}";
        System.out.println(jsonCreate);
        String bearer = sendPostLoginUserAndGetBearer(jsonCreate);
        String json = "[ {\"op\": \"replace\", \"path\": \"/email\", \"value\": \"123456789@Gmail.com\"}]";
        Response response = refreshUser(bearer, json);
        String body = "<{email=123456789@Gmail.com, name=" + name +">";
        compareUserStatusCodeAndMessage(response, 200, body);
        printResponseBodyToConsole(response);
    }

    
    @Test
    @DisplayName("Проверка изменения имени пользователя")
    public void checkUserCanChangeLoginWithoutAuthorization() {
        String bearer = "";
        String json = "[ {\"op\": \"replace\", \"path\": \"/email\", \"value\": \"123456789@Gmail.com\"}]";
        Response response = refreshUser(bearer, json);
        compareUserStatusCode(response,401);
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

    @Step("Send PATCH request to api/auth/user")
    public Response refreshUser (String bearer, String json){
        Response response = given()
                .header("Authorization", bearer)
                .body(json)
                .when()
                .patch("api/auth/user");
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
