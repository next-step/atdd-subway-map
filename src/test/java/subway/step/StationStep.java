package subway.step;

import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.StationResponse;

public class StationStep {

  public static Map 지하철역_생성_요청 (String 역_이름) {
      return RestAssured.given()
          .body(Map.of("name", 역_이름))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .post("/stations")
          .then().log().all()
          .assertThat()
          .statusCode(HttpStatus.CREATED.value())
          .extract()
          .jsonPath()
          .get("$");
  }

  public static List<Map> 지하철역_다중_생성_요청 (List<String> 역_목록) {
    List<Map> responses = new ArrayList<>();

    for (String 역이름 : 역_목록) {
      responses.add(RestAssured.given()
          .body(Map.of("name", 역이름))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .post("/stations")
          .then()
          .assertThat()
          .statusCode(HttpStatus.CREATED.value())
          .extract()
          .jsonPath()
          .get("$")
      );
    }

    return responses;
  }

  public static List<String> 지하철역_이름_전체조회_요청 () {
    return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/stations")
            .then().log().all()
            .extract()
            .jsonPath()
            .getList("name", String.class);
  }
}
