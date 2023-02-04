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
import subway.web.response.LineLoadDtoResponse;
import subway.web.response.SectionResponse;
import subway.web.response.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
class SectionAcceptanceTest extends BaseAcceptance {

    StationResponse 강남역;
    StationResponse 논현역;
    LineLoadDtoResponse 신분당선;

    @BeforeEach
    void setUpStation() {
        강남역 = 지하철역_생성("강남역").as(StationResponse.class);
        논현역 = 지하철역_생성("논현역").as(StationResponse.class);
        신분당선 = 지하철_노선_생성("신분당선", 강남역, 논현역);
    }

    /**
     * When 지하철 구간을 생성하면
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void 지하철_구간_생성을_생성_할_수_있다() {
        // Given
        StationResponse 신논현역 = 지하철역_생성("신논현역").as(StationResponse.class);

        // When
        SectionResponse sectionResponse = 지하철_구간_생성(신분당선, 신논현역, 논현역, 10L).as(SectionResponse.class);

        // Then
        ExtractableResponse<Response> actualSection = 지하철_구간_목록_요청(sectionResponse);

        지하철_구간을_조회_할_수_있다(sectionResponse, actualSection);
        지하철_노선을_확인_할_수_있다(sectionResponse);
    }

    private void 지하철_노선을_확인_할_수_있다(SectionResponse givenSection) {
        ExtractableResponse<Response> actualResponse = 지하철_노선_조회(givenSection.getLineResponse().getId());

        LineLoadDtoResponse lineLoadDtoResponse = actualResponse.as(LineLoadDtoResponse.class);

        assertThat(lineLoadDtoResponse.getStations()).containsExactlyInAnyOrderElementsOf(List.of(강남역, 논현역, givenSection.getDownStation()));

    }

    /**
     * 지하철 노선에 구간을 등록하는 기능을 구현
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */


    /**
     * When 지하철 구간을 생성시 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닐 시
     * Then throw Exception
     */
    @DisplayName("지하철 구간 생성 시 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닐 시 Exception")
    @Test
    void 지하철_구간_생성_시_사행역은_해당_노선에_등록되어있는_하행_종점역이_아닐_시_Exception() {
        // Given && When
        ExtractableResponse<Response> actualResponse = 지하철_구간_생성(신분당선, 강남역, 논현역, 10L);

        // Then
        지하철_구간_등록하려는_상행역이_기존_하행역이_아니다(actualResponse);
    }

    private void 지하철_구간_등록하려는_상행역이_기존_하행역이_아니다(ExtractableResponse<Response> actualSection) {
        assertThat(actualSection.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static ExtractableResponse<Response> 지하철_구간_목록_요청(SectionResponse givenSection) {
        ExtractableResponse<Response> sectionResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("sectionId", givenSection.getId())
            .when().get("/sections/{sectionId}")
            .then().log().all()
            .extract();

        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return sectionResponse;
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

    private static ExtractableResponse<Response> 지하철_구간_생성(LineLoadDtoResponse line, StationResponse downStation, StationResponse upStation, Long distance) {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(downStation.getId(), upStation.getId(), distance);

        ExtractableResponse<Response> createResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", line.getId())
            .body(sectionCreateRequest)
            .when().post("/lines/{lineId}/sections")
            .then().log().all()
            .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createResponse;
    }

    private static void 지하철_구간을_조회_할_수_있다(SectionResponse givenSection, ExtractableResponse<Response> actualSection) {
        assertThat(actualSection.as(SectionResponse.class)).isEqualTo(givenSection);
    }

    private LineLoadDtoResponse 지하철_노선_생성(String lineName, StationResponse upStation, StationResponse downStation) {
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

        return createResponse.as(LineLoadDtoResponse.class);
    }

    private ExtractableResponse<Response> 지하철_노선_조회(Long lineId) {
        return RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", lineId)
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract();
    }

}
