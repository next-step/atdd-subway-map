package subway.step;

import io.restassured.RestAssured;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LineSectionStep {

  public static Map 지하철_구간_생성(Long id) {
    return RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", id)
        .post("/lines/{id}/sections")
        .then().log().all()
        .assertThat()
        .statusCode(HttpStatus.CREATED.value())
        .extract()
        .jsonPath()
        .get("$");
  }

  public static Map 지하철_구간_조회(Long id) {
    return RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .pathParam("id", id)
        .get("/lines/{id}/sections")
        .then().log().all()
        .assertThat()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .jsonPath()
        .get("$");
  }
}
