package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.application.dto.StationResponse;
import subway.fixture.LineFixture;
import subway.fixture.StationFixture;
import subway.utils.DatabaseCleanup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("acceptance")
public class LineAcceptanceTest {

  @Autowired
  private DatabaseCleanup databaseCleanup;

  @BeforeEach
  void setUp() {
    databaseCleanup.execute();
  }

  /**
   * When 지하철 노선을 생성하면 Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @DisplayName("지하철 노선을 생성한다.")
  @Test
  void createLine() {
    // when
    Long 강남역 = 지하철역_생성_요청("강남역");
    Long 역삼역 = 지하철역_생성_요청("역삼역");

    Map<String, Object> params = new HashMap<>();
    params.put("name", "이호선");
    params.put("color", "green");
    params.put("upStationId", 강남역);
    params.put("downStationId", 역삼역);
    params.put("distance", 10);
    ExtractableResponse<Response> response = LineFixture.지하철노선_생성_요청(params);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    // then
    List<String> lineNames =
        RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    assertThat(lineNames).containsAnyOf("이호선");
  }

  /**
   * Given 2개의 지하철 노선을 생성하고 When 지하철 노선 목록을 조회하면 Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @DisplayName("지하철 노선 목록을 조회한다.")
  @Test
  void getLines() {
    // given
    Long 강남역 = 지하철역_생성_요청("강남역");
    Long 역삼역 = 지하철역_생성_요청("역삼역");
    Long 신논현역 = 지하철역_생성_요청("신논현역");

    ExtractableResponse<Response> 신분당선 = LineFixture.지하철노선_생성_요청(
        Map.of(
            "name", "신분당선",
            "color", "red",
            "upStationId", 강남역,
            "downStationId", 신논현역,
            "distance", 10
        )
    );
    ExtractableResponse<Response> 이호선 = LineFixture.지하철노선_생성_요청(
        Map.of(
            "name", "이호선",
            "color", "green",
            "upStationId", 강남역,
            "downStationId", 역삼역,
            "distance", 10
        )
    );

    // when
    List<String> lineNames = LineFixture.getLines().jsonPath().getList("name", String.class);

    // then
    assertThat(lineNames).containsAnyOf("신분당선", "이호선");
  }

  /**
   * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 조회하면 Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
   */
  @DisplayName("지하철노선을 조회한다.")
  @Test
  void getLine() {
    // given
    Long 강남역 = 지하철역_생성_요청("강남역");
    Long 역삼역 = 지하철역_생성_요청("역삼역");

    ExtractableResponse<Response> 이호선 = LineFixture.지하철노선_생성_요청(
            Map.of(
                    "name", "이호선",
                    "color", "green",
                    "upStationId", 강남역,
                    "downStationId", 역삼역,
                    "distance", 10
            )
    );

    // when
    String lineName =
        RestAssured.given().log().all()
            .pathParam("lineId", 이호선.jsonPath().getLong("id"))
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract().jsonPath().getString("name");

    // then
    assertThat(lineName).isEqualTo("이호선");
  }

  /**
   * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 수정하면 Then 해당 지하철 노선 정보는 수정된다
   */
  @DisplayName("지하철 노선을 수정한다.")
  @Test
  void updateLine() {
    // given
    Long 강남역 = 지하철역_생성_요청("강남역");
    Long 역삼역 = 지하철역_생성_요청("역삼역");

    ExtractableResponse<Response> 이호선 = LineFixture.지하철노선_생성_요청(
            Map.of(
                    "name", "이호선",
                    "color", "red",
                    "upStationId", 강남역,
                    "downStationId", 역삼역,
                    "distance", 10
            )
    );

    // when
    final Map<String, Object> updateParams = new HashMap<>();
    updateParams.put("name", "이호선new");
    updateParams.put("color", "green");

    ExtractableResponse<Response> updateResponse =
        RestAssured.given().log().all()
            .body(updateParams)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("lineId", 이호선.jsonPath().getLong("id"))
            .when().put("/lines/{lineId}")
            .then().log().all()
            .extract();

    // then
    assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  /**
   * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 삭제하면 Then 해당 지하철 노선 정보는 삭제된다
   */
  @DisplayName("지하철 노선을 제거한다.")
  @Test
  void deleteLine() {
    // given
    Long 강남역 = 지하철역_생성_요청("강남역");
    Long 역삼역 = 지하철역_생성_요청("역삼역");

    ExtractableResponse<Response> 이호선 = LineFixture.지하철노선_생성_요청(
            Map.of(
                    "name", "이호선",
                    "color", "red",
                    "upStationId", 강남역,
                    "downStationId", 역삼역,
                    "distance", 10
            )
    );

    // when
    ExtractableResponse<Response> deleteResponse =
        RestAssured.given().log().all()
            .pathParam("lineId", 이호선.jsonPath().getLong("id"))
            .when().delete("/lines/{lineId}")
            .then().log().all()
            .extract();

    // then
    assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }


  private static Long 지하철역_생성_요청(String name) {
    return StationFixture.지하철역_생성_요청(name).as(StationResponse.class).getId();
  }
}
