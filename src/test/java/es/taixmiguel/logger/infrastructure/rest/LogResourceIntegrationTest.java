package es.taixmiguel.logger.infrastructure.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class LogResourceIntegrationTest {
    @Test
    void shouldReturn400WhenMessageIsBlank() {
        String invalidJson = """
            { "timestamp": "...", "level": "info", "message": " " }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(invalidJson)
        .when()
            .post("/api/logs/billing-app")
        .then()
            .statusCode(400)
            .log().body();
    }
}
