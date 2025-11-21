package de.seuhd.campuscoffee.acctest;

import de.seuhd.campuscoffee.api.dtos.PosDto;
import io.restassured.RestAssured;
import java.util.List;

public class TestUtils {

    // POS listesi al
    public static List<PosDto> retrievePos() {
        return RestAssured.given()
                .contentType("application/json")
                .get("/api/pos")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", PosDto.class);
    }


    public static PosDto getPosByName(String name) {
        return retrievePos().stream()
                .filter(pos -> pos.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("POS not found: " + name));
    }


    public static void updatePos(PosDto pos) {
        RestAssured.given()
                .contentType("application/json")
                .body(pos)
                .put("/api/pos/" + pos.getId())
                .then()
                .statusCode(200);
    }


    public static List<PosDto> createPos(List<PosDto> posList) {
        posList.forEach(pos ->
                RestAssured.given()
                        .contentType("application/json")
                        .body(pos)
                        .post("/api/pos")
                        .then()
                        .statusCode(200)
        );
        return retrievePos();
    }
}