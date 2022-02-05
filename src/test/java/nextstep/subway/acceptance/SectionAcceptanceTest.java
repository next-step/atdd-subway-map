package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.createLineRequest;
import static nextstep.subway.acceptance.StationSteps.createStationRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        createStationRequest("강남역");
        createStationRequest("양재역");
        createStationRequest("양재시민의숲역");
        createStationRequest("청계산입구역");
        createStationRequest("판교역");
    }


    /**
     * Scenario: 구간 추가하기
     *
     * Given 지하철 노선 생성을 요청하고
     * When 생성한 노선에 지하철 구간 생성을 요청하면
     * Then 요청한 노선에 지하철 구간이 추가된다.
     */
    @DisplayName("지하철 구간 추가하기")
    @Test
    void addSection() {
        // given
        ExtractableResponse<Response> createLineResponse = createLineRequest("신분당선", "bg-red-600", 4L, 2L, 10);
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "5");
        params.put("distance", "10");

        // when
        String uri = createLineResponse.header("Location") + "/sections";
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(uri)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Scenario: 노선에 등록되어 있는 역으로 구간을 생성할 수 없다.
     *
     * Given 지하철 노선 생성을 요청하고
     * When 노선에 등록되어있는 역을 하행선으로 구간 등록을 요청하면
     * Then 노선에 구간 추가가 실패한다.
     */
    @DisplayName("지하철 구간 추가 실패 - 노선에 이미 등록되어있는 역을 하행역으로 생성할 경우")
    @Test
    void addSectionWithExistStationInLine() {
        // given
        ExtractableResponse<Response> createLineResponse = createLineRequest("신분당선", "bg-red-600", 4L, 2L, 10);
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "4");
        params.put("distance", "10");

        // when
        String uri = createLineResponse.header("Location") + "/sections";
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(uri)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
