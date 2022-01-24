package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    /**
     * Given 지하철 노선 생성을 요청하고
     * Given 상행역 생성을 요청하고
     * Given 하행역 생성을 요청하고
     * When 구간 생성을 요청 하면
     * Then 구간 생성이 성공한다.
     */
    @DisplayName("구간 생성")
    @Test
    void createSection() {
        // given
        ExtractableResponse<Response> createLineResponse = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성());
        ExtractableResponse<Response> createStationResponse1 = StationSteps.지하철역_생성("강남역");
        ExtractableResponse<Response> createStationResponse2 = StationSteps.지하철역_생성("역삼역");

        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", createStationResponse1.body().jsonPath().getLong("id"));
        params.put("downStationId", createStationResponse2.body().jsonPath().getLong("id"));
        params.put("distance", 10);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
