package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.web.request.LineCreateRequest;
import subway.web.request.SectionCreateRequest;
import subway.web.response.LineResponse;
import subway.web.response.SectionResponse;
import subway.web.response.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
class SectionAcceptanceTest extends BaseAcceptance {

    @BeforeEach
    void setUpStation() {
        StationResponse 강남역 = 지하철역_생성("강남역").as(StationResponse.class);
        StationResponse 논현역 = 지하철역_생성("논현역").as(StationResponse.class);
        LineResponse 신분당선 = 지하철_노선_생성("신분당선", 강남역, 논현역);
    }

    /**
     * 지하철 노선에 구간을 등록하는 기능을 구현
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */

    /**
     * When 지하철 구간을 생성하면
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void createSection() {
        // Given && When
        SectionResponse givenSection = 지하철_구간_생성();

        // Then
        ExtractableResponse<Response> actualSection = 지하철_구간_목록_요청(givenSection);

        지하철_구간을_조회_할_수_있다(givenSection, actualSection);
    }

    private static void 지하철_구간을_조회_할_수_있다(SectionResponse givenSection, ExtractableResponse<Response> actualSection) {
        assertThat(actualSection.as(SectionResponse.class)).isEqualTo(givenSection);
    }

    private static ExtractableResponse<Response> 지하철_구간_목록_요청(SectionResponse givenSection) {
        ExtractableResponse<Response> sectionResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .param("sectionId", givenSection.getId())
            .when().get("/lines/1/sections/{sectionId}")
            .then().log().all()
            .extract();

        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return sectionResponse;
    }

    private static SectionResponse 지하철_구간_생성() {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("1", "2", 10L);

        ExtractableResponse<Response> createResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(sectionCreateRequest)
            .when().post("/lines/1/sections")
            .then().log().all()
            .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createResponse.as(SectionResponse.class);
    }

    private LineResponse 지하철_노선_생성(String lineName, StationResponse upStation, StationResponse downStation) {
        LineCreateRequest givenRequest = new LineCreateRequest(lineName, "bg-red-600", upStation.getId(), downStation.getId(), 10L);

        ExtractableResponse<Response> createResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(givenRequest)
            .when().post("/lines")
            .then().log().all()
            .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.jsonPath().getString("name")).isEqualTo(givenRequest.getName());
        assertThat(createResponse.jsonPath().getString("color")).isEqualTo(givenRequest.getColor());
        List<StationResponse> stations = createResponse.jsonPath().getList("stations", StationResponse.class);
        assertThat(stations).containsExactlyInAnyOrder(upStation, downStation);

        return createResponse.as(LineResponse.class);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회(LineResponse line) {
        return RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", line.getId())
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(params)
            .when().post("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

}
