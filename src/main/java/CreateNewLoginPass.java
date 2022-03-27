import org.apache.commons.lang3.RandomStringUtils;



public class CreateNewLoginPass {
    public String registerNewLoginPass(){

        // с помощью библиотеки RandomStringUtils генерируем логин
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        String userLogin = RandomStringUtils.randomAlphabetic(15);
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String userPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя курьера
        String userName = RandomStringUtils.randomAlphabetic(10);

        // создаём список, чтобы метод мог его вернуть


        // собираем в строку тело запроса на регистрацию, подставляя в него логин, пароль и имя курьера
        String registerRequestBody = "{\"email\":\"" + userLogin + "@yandex.ru" + "\","
                + "\"password\":\"" + userPassword + "\","
                + "\"name\":\"" + userName + "\"}";



        // возвращаем список
        return registerRequestBody;

    }

}