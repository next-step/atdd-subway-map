package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.fixture.LineFixture;
import subway.fixture.StationFixture;
import subway.application.dto.LineResponse;
import subway.application.dto.StationResponse;

@DisplayName("구간 관리 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

  /**
   * Given 지하철 노선이 생성을 요청 하고
   * When 지하철 노선에 새로운 구간 추가를 요청 하면
   * Then 노선에 새로운 구간이 추가된다.
   */
  @DisplayName("구간을 등록한다.")
  @Test
  void createSection() {
    // given
    Long 강남역 = 지하철역_생성_요청("강남역");
    Long 역삼역 = 지하철역_생성_요청("역삼역");
    Long 선릉역 = 지하철역_생성_요청("선릉역");

    Long 이호선 = 지하철노선_생성_요청(강남역, 역삼역);

    // when
    Map<String, Object> params = new HashMap<>();
    params.put("upStationId", 역삼역);
    params.put("downStationId", 선릉역);
    params.put("distance", 10);

    ExtractableResponse<Response> response = RestAssured
        .given().body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().post("/lines/{lineId}/sections", 이호선)
        .then().extract();

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  private static Long 지하철노선_생성_요청(Long 상행역, Long 하행역) {
    Map<String, Object> params = new HashMap<>();
    params.put("name", "2호선");
    params.put("color", "bg-green-600");
    params.put("upStationId", 상행역);
    params.put("downStationId", 하행역);
    params.put("distance", 10);
    return LineFixture.지하철노선_생성_요청(params).as(LineResponse.class).getId();
  }

  private static Long 지하철역_생성_요청(String name) {
    return StationFixture.지하철역_생성_요청(name).as(StationResponse.class).getId();
  }
}
