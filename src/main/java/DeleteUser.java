import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteUser {

    public String DeleteUser(String json) {
        String answer = "";
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("api/auth/login");
        UserData userBearer = response.body().as(UserData.class);
        String bearer = userBearer.getAccessToken();

        Response responseDel = given()
                .header("Authorization", bearer)
                .delete("api/auth/user");
        if (responseDel.statusCode() == 202) {
            String an = "Учетная запись удалена";
            answer = an + " : " + responseDel.body().asString();
        }
        else {
            answer = responseDel.body().asString();
        }
        return answer;
    }
}
