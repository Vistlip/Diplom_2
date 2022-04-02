import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteUser {

    public void deleteUser(String json) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("api/auth/login")
                .then()
                .log().body()
                .extract().response();
        UserData userBearer = response.body().as(UserData.class);
        String bearer = userBearer.getAccessToken();

        Response responseDel = given()
                .header("Authorization", bearer)
                .delete("api/auth/user");
    }
}
