import io.restassured.response.Response;
// импортируем библиотеку генерации строк
import org.apache.commons.lang3.RandomStringUtils;
// импортируем список
import java.util.ArrayList;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;

public class BurgerRegisterUser {


    public ArrayList<String> registerNewUserAndReturnLoginPassword(){

        // с помощью библиотеки RandomStringUtils генерируем логин
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        String userLogin = RandomStringUtils.randomAlphabetic(15);
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String userPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя курьера
        String userName = RandomStringUtils.randomAlphabetic(10);

        // создаём список, чтобы метод мог его вернуть
        ArrayList<String> loginPass = new ArrayList<>();

        // собираем в строку тело запроса на регистрацию, подставляя в него логин, пароль и имя курьера
        String registerRequestBody = "{\"email\":\"" + userLogin + "@yandex.ru" + "\","
                + "\"password\":\"" + userPassword + "\","
                + "\"name\":\"" + userName + "\"}";

        // отправляем запрос на регистрацию курьера и сохраняем ответ в переменную response класса Response
        Response response =  given()
                .header("Content-type", "application/json")

                .and()
                .body(registerRequestBody)
                .when()
                .post("https://stellarburgers.nomoreparties.site/api/auth/register");

        // если регистрация прошла успешно (код ответа 200), добавляем в список логин и пароль курьера
        if (response.statusCode() == 200) {
            loginPass.add(userLogin);
            loginPass.add(userPassword);
            loginPass.add(userName);
        }

        // возвращаем список
        return loginPass;

    }

}
