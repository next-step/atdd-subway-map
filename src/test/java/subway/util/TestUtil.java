package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.line.LineRequest;
import subway.dto.section.SectionRequest;

import static io.restassured.RestAssured.given;

public class TestUtil {
    public static Response createStation(String stationName) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"name\":\""+ stationName +"\"}")
                .when()
                .post("/stations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response();
    }

    public static Response createLine(LineRequest lineRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when()
                .post("/lines")
                .then().log().all()
                .extract().response();
    }

    public static Response createSection(String url, SectionRequest sectionRequest) {
        return  given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post(url)
                .then().log().all()
                .extract().response();
    }

    public static Response showLine(String url) {
        return given().log().all()
                .when().get(url)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
    }
}
