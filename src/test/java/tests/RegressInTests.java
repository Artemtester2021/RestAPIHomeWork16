package tests;

import models.lombok.DataBodyLombokModel;
import models.lombok.DataResponseLombokModel;
import models.pojo.DataBodyModel;
import models.pojo.DataResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.ReqresInSpec.*;

@Tag("restApi")
public class RegressInTests extends TestBase {

    int validUserId = 2;
    int notValidUserId = 23;
    int notResourceUserId = 23;

    @Test
    @DisplayName("Запрос данных по несуществующему пользователю")
    void singleUserNotFoundTest() {
        String response = step("Запрос пользователя", () -> given(requestSpec)
                .when()
                .get(USERS_END_POINT + notValidUserId)
                .then()
                .spec(ResponseSpec)
                .extract().asString());
        step("Check response", () -> {
                assertEquals("{}", response, "Тело ответа пустое");
        });
    }

    @Test
    @DisplayName("Ни один ресурс не найден")
    void SingleResourceNotFound() {
        String response = step("Запрос удаления пользователя", () -> given(requestSpec)
                .when()
                .get(RESOURS_END_POINT + notResourceUserId)
                .then()
                .spec(ResponseSpec)
                .extract().asString());
        step("Check response", () -> {
            assertEquals("{}", response, "json пустой");
        });
    }

    @Test
    @DisplayName("Обновление имени пользователя (через put)")
    void updateLombokTest() {
        DataBodyLombokModel bodyDate = new DataBodyLombokModel();
        bodyDate.setName("morpheus");
        DataResponseLombokModel response = step("Запрос на обновление имени", () ->
                given(requestSpec)
                        .body(bodyDate)
                        .when()
                        .put(USERS_END_POINT + validUserId)
                        .then()
                        .spec(ResponseSpec)
                        .extract().as(DataResponseLombokModel.class));
        step("Check response", () ->
                assertEquals("morpheus", response.getName()));
    }

    @Test
    @DisplayName("Изменение должности пользователя (через patch)")
    void successfulPatchUserJobPojoTest() {
        DataBodyModel bodyDate = new DataBodyModel();
        bodyDate.setName("zion resident");
        DataResponseModel response = step("Запрос на изменение должности", () ->
                given(requestSpec)
                        .body(bodyDate)
                        .when()
                        .patch(USERS_END_POINT + validUserId)
                        .then()
                        .spec(ResponseSpec)
                        .extract().as(DataResponseModel.class));
        step("Check response", () ->
                assertEquals("zion resident", response.getName()));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void successfulDeleteUserTest() {
        String response = step("Запрос удаления пользователя", () -> given(requestSpec)
                .when()
                .delete(USERS_END_POINT + validUserId)
                .then()
                .spec(userDeleteResponseSpec)
                .extract().asString());
        step("Check response", () -> {
            assertEquals("", response, "Ответ пустой");
        });
    }
}