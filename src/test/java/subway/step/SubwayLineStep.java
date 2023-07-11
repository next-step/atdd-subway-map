package subway.step;

import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.SubwayLineEditRequest;
import subway.line.SubwayLineRequest;
import subway.line.LineResponse;

public class SubwayLineStep {

  public static Map 지하철_노선_생성(Map<String, Object> body) {
    return RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(body)
        .post("/lines")
        .then().log().all()
        .assertThat()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .jsonPath()
        .get("$");
  }

  public static LineResponse 지하철_노선_생성(SubwayLineRequest 신규노선) {
    return RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(신규노선)
        .post("/lines")
        .then()
        .assertThat()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .as(LineResponse.class);
  }

  public static LineResponse 노선조회(Long 역_ID) {
    return RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", 역_ID)
        .get("/lines/{id}")
        .then()
        .assertThat()
        .extract()
        .as(LineResponse.class);
  }

  public static void 노선이_존재하지_않음(Long 역_ID) {
    RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", 역_ID)
        .get("/lines/{id}")
        .then()
        .assertThat()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  public static List<LineResponse> 전체_노선조회() {
    return Arrays.asList(
        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .get("/lines")
            .then()
            .assertThat()
            .extract()
            .as(LineResponse[].class)
    );
  }

  public static void 지하철_노선_수정(Long 역_ID, SubwayLineEditRequest 기존_노선_수정) {
    RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(기존_노선_수정)
        .pathParam("id", 역_ID)
        .patch("/lines/{id}")
        .then()
        .assertThat()
        .statusCode(HttpStatus.OK.value());
  }

  public static void 지하철_노선_삭제(Long 역_ID) {
    RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", 역_ID)
        .delete("/lines/{id}")
        .then()
        .assertThat()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }
}
