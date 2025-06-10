package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.is;


public class RegressInTests extends TestBase {

    int validUserId = 2;
    int notValidUserId = 23;
    int notResourceUserId = 23;

    @Test
    @DisplayName("Запрос данных по несуществующему пользователю")
    void singleUserNotFoundTest(){
        given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .log().uri()
                .when()
                .get(TestBase.USERS_END_POINT + notValidUserId)
                .then()
                .log().status()
                .statusCode(HTTP_NOT_FOUND)
                .body(is("{}"));
    }

    @Test
    @DisplayName("Ни один ресурс не найден")
    void SingleResourceNotFound(){
        given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .log().uri()
                .when()
                .get(TestBase.RESOURS_END_POINT + notResourceUserId)
                .then()
                .log().status()
                .statusCode(HTTP_NOT_FOUND)
                .body(is("{}"));
    }

    @Test
    @DisplayName("Обновление имени пользователя (через put)")
    void updateTest(){
        String newBody = "{\"name\": \"morpheus\"}";
        given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .body(newBody)
                .contentType(JSON)
                .log().uri()
                .when()
                .put(TestBase.USERS_END_POINT + validUserId)
                .then()
                .log().status()
                .statusCode(HTTP_OK)
                .body("name", is( "morpheus"));
    }

    @Test
    @DisplayName("Изменение должности пользователя (через patch)")
    void successfulPatchUserJobTest() {
        String newUserDataBody = "{\"job\": \"zion resident\"}";

        given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .body(newUserDataBody)
                .contentType(JSON)
                .log().uri()
                .when()
                .patch(TestBase.USERS_END_POINT + validUserId)
                .then()
                .log().status()
                .log().body()
                .statusCode(HTTP_OK)
                .body("job", is("zion resident"));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void successfulDeleteUserTest() {
        given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .contentType(JSON)
                .log().uri()
                .when()
                .delete(TestBase.USERS_END_POINT + validUserId)
                .then()
                .log().status()
                .statusCode(HTTP_NO_CONTENT);
    }
}
