package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/subway.sql")
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

    StationResponse donongStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "구리역");

    StationResponse gooriStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "1호선");
    params.put("color", Color.BLUE);
    params.put("upStationId", donongStation.getId());
    params.put("downStationId", gooriStation.getId());
    params.put("distance", 10);

    ExtractableResponse<Response> response = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();

    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    ExtractableResponse<Response> response1 = RestAssured.given().log().all()
        .when().get("/lines")
        .then().log().all()
        .extract();

    List<String> names = response1.jsonPath().getList("name", String.class);
    assertThat(names).containsAnyOf("1호선");

    List<String> colors = response1.jsonPath().getList("color", String.class);
    assertThat(colors).containsAnyOf(Color.BLUE.name());
  }

  /**
   * Given 2개의 지하철 노선을 생성하고
   * When 지하철 노선 목록을 조회하면
   * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @Test
  void 지하철_노선_목록_조회() {
    Map<String, Object> params = new HashMap<>();
    params.put("name", "도농역");

    StationResponse donongStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "구리역");

    StationResponse gooriStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "덕소역");

    StationResponse ducksoStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "1호선");
    params.put("color", Color.BLUE);
    params.put("upStationId", donongStation.getId());
    params.put("downStationId", gooriStation.getId());
    params.put("distance", 10);

    ExtractableResponse<Response> donongGooriLine = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();

    assertThat(donongGooriLine.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    params.put("name", "2호선");
    params.put("color", Color.GREEN);
    params.put("upStationId", donongStation.getId());
    params.put("downStationId", ducksoStation.getId());
    params.put("distance", 10);

    ExtractableResponse<Response> donongDucksoLine = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();

    assertThat(donongDucksoLine.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    ExtractableResponse<Response> response = RestAssured.given().log().all()
        .when().get("/lines")
        .then().log().all()
        .extract();

    List<String> names = response.jsonPath().getList("name", String.class);
    assertThat(names).containsAnyOf("1호선", "2호선");

    List<String> colors = response.jsonPath().getList("color", String.class);
    assertThat(colors).containsAnyOf(Color.BLUE.name(), Color.GREEN.name());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 조회하면
   * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
   */
  @Test
  void 지하철_노선_조회() {
    Map<String, Object> params = new HashMap<>();
    params.put("name", "도농역");

    StationResponse donongStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "구리역");

    StationResponse gooriStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "1호선");
    params.put("color", Color.BLUE);
    params.put("upStationId", donongStation.getId());
    params.put("downStationId", gooriStation.getId());
    params.put("distance", 10);

    ExtractableResponse<Response> donongGooriLine = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();

    assertThat(donongGooriLine.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    ExtractableResponse<Response> response = RestAssured.given().log().all()
        .when().get("/lines/" + donongGooriLine.jsonPath().getLong("id"))
        .then().log().all()
        .extract();

    String names = response.jsonPath().getString("name");
    assertThat(names).isEqualTo("1호선");

    String colors = response.jsonPath().getString("color");
    assertThat(colors).isEqualTo(Color.BLUE.name());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 수정하면
   * Then 해당 지하철 노선 정보는 수정된다
   */
  @Test
  void 지하철_노선_수정() {
    Map<String, Object> params = new HashMap<>();
    params.put("name", "도농역");

    StationResponse donongStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "구리역");

    StationResponse gooriStation = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/stations")
        .then().log().all()
        .extract().as(StationResponse.class);

    params.put("name", "1호선");
    params.put("color", Color.BLUE);
    params.put("upStationId", donongStation.getId());
    params.put("downStationId", gooriStation.getId());
    params.put("distance", 10);

    ExtractableResponse<Response> donongGooriLine = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines")
        .then().log().all()
        .extract();

    assertThat(donongGooriLine.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    params.put("name", "3호선");
    params.put("color", Color.ORANGE);

    ExtractableResponse<Response> updateDonongGooriLine = RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().put("/lines/" + donongGooriLine.jsonPath().getLong("id"))
        .then().log().all()
        .extract();

    assertThat(updateDonongGooriLine.statusCode()).isEqualTo(HttpStatus.OK.value());
  }
}
