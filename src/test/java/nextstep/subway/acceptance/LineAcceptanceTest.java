package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.domain.Color;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }

  /**
   * When 지하철 노선을 생성하면
   * Then 지하철역이 생성된다
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @Test
  void 지하철_노선_생성() {
    Map<String, Object> params = new HashMap<>();
    params.put("name", "도농역");

    Station gangnamStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(Station.class);

    params.put("name", "구리역");

    Station gooriStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(Station.class);

    params.put("name", "1호선");
    params.put("color", Color.BLUE);
    params.put("upStationId", gangnamStation);
    params.put("downStationId", gooriStation);
    params.put("distance", 10);

    ExtractableResponse<Response> response = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();

    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    Line line = RestAssured.given().log().all()
        .when().get("/lines")
        .then().log().all()
        .extract().as(Line.class);

    assertThat(line.getName()).isEqualTo("1호선");
    assertThat(line.getColor()).isEqualTo(Color.BLUE);
    assertThat(line.getUpStationId()).isEqualTo(gangnamStation.getId());
    assertThat(line.getDownStationId()).isEqualTo(gooriStation.getId());
    assertThat(line.getDistance()).isEqualTo(10);
  }
}
