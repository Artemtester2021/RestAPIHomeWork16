package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.lombok.DataBodyLombokModel;
import models.lombok.DataResponseLombokModel;
import models.pojo.DataBodyModel;
import models.pojo.DataResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("restApi")
public class RegressInTests extends TestBase {

    int validUserId = 2;
    int notValidUserId = 23;
    int notResourceUserId = 23;

    @Test
    @DisplayName("Запрос данных по несуществующему пользователю")
    void singleUserNotFoundTest() {
        given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .filter(withCustomTemplates())
                .log().uri()
                .when()
                .get(TestBase.USERS_END_POINT + notValidUserId)
                .then()
                .log().status()
                .log().body()
                .log().headers()
                .statusCode(HTTP_NOT_FOUND)
                .body(is("{}"));
    }

    @Test
    @DisplayName("Ни один ресурс не найден")
    void SingleResourceNotFound() {
        given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .log().uri()
                .when()
                .get(TestBase.RESOURS_END_POINT + notResourceUserId)
                .then()
                .log().status()
                .log().body()
                .log().headers()
                .statusCode(HTTP_NOT_FOUND)
                .body(is("{}"));
    }

    @Test
    @DisplayName("Обновление имени пользователя (через put)")
    void updateLombokTest() {
        DataBodyLombokModel bodyDate = new DataBodyLombokModel();
        bodyDate.setName("morpheus");

        DataResponseLombokModel response = given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .body(bodyDate)
                .contentType(JSON)
                .log().uri()
                .when()
                .put(TestBase.USERS_END_POINT + validUserId)
                .then()
                .log().status()
                .log().body()
                .log().headers()
                .statusCode(HTTP_OK)
                .extract().as(DataResponseLombokModel.class);

        assertEquals("morpheus", response.getName());
    }

    @Test
    @DisplayName("Изменение должности пользователя (через patch)")
    void successfulPatchUserJobPojoTest() {
        DataBodyModel bodyDate = new DataBodyModel();
        bodyDate.setName("zion resident");

        DataResponseModel response = given()
                .header(TestBase.FREE_API_KEY_NAME, TestBase.FREE_API_KEY_VALUE)
                .body(bodyDate)
                .contentType(JSON)
                .log().uri()
                .when()
                .patch(TestBase.USERS_END_POINT + validUserId)
                .then()
                .log().status()
                .log().body()
                .log().headers()
                .statusCode(HTTP_OK)
                .extract().as(DataResponseModel.class);

        assertEquals("zion resident", response.getName());
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
                .log().body()
                .log().headers()
                .statusCode(HTTP_NO_CONTENT);
    }
}
