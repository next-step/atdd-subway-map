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
    StationResponse 신논현역;
    LineLoadDtoResponse 신분당선;

    @BeforeEach
    void setUpStation() {
        강남역 = 지하철역_생성("강남역");
        논현역 = 지하철역_생성("논현역");
        신논현역 = 지하철역_생성("신논현역");
        신분당선 = 지하철_노선_생성("신분당선", 강남역, 논현역);
    }

    /**
     * When 지하철 구간을 생성하면
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void 지하철_구간_생성을_생성_할_수_있다() {
        // When
        ExtractableResponse<Response> sectionResponse = 지하철_구간_생성(신분당선, 논현역, 신논현역, 10L);

        // Then
        SectionResponse actualSection = 지하철_구간_목록_요청(sectionResponse);

        지하철_구간을_조회_할_수_있다(sectionResponse, actualSection);
        지하철_노선을_확인_할_수_있다(actualSection);
    }


    /**
     * When
     * Then 기존 노선에 새로운 구간 생성 시 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닐 시 생성이 안된다
     */
    @DisplayName("새로운 구간 생성 시 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다")
    @Test
    void 지하철_구간_생성_시_상행역은_해당_노선에_등록되어있는_하행_종점역이_아닐_시_생성이_안된다() {
        // When && Then
        ExtractableResponse<Response> actualResponse = 지하철_구간_생성_요청(신분당선, 논현역, 강남역, 10L);

        지하철_구간_생성이_안된다(actualResponse);
    }

    /**
     * When 기존 노선에 새로운 구간 요청 시 만약 하행역이 해당 노선에 등록되어있는 역일 경우
     * Then 생성이 안된다
     */
    @DisplayName("새로운 구간 생성 시 하행역은 해당 노선에 등록되어있는 역일 수 없다")
    @Test
    void 지하철_구간_생성_시_하행역이_해당_노선에_등록되어있는_역이_경우_등록이_안된다() {
        // When
        ExtractableResponse<Response> actualResponse = 지하철_구간_생성_요청(신분당선, 논현역, 강남역, 10L);

        // Then
        지하철_구간_생성이_안된다(actualResponse);
    }

    private void 지하철_구간_생성이_안된다(ExtractableResponse<Response> actualResponse) {
        assertThat(actualResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 새로운 역을 추가 후
     * Given 지하철 노선에 새로운 구간을 추가 후
     * When 구간 제거 시
     * Then 노선에 구간을 확인 할 수 없다
     */
    @DisplayName("지하철 노선에 구간을 제거 할 수 있다")
    @Test
    void 지하철_노선에_구간을_제거_할_수_있다() {
        // Given
        지하철_구간_생성(신분당선, 논현역, 신논현역, 10L);

        List<SectionResponse> 노선의_구간들 = 지하철_노선의_구간들_조회_요청(신분당선);
        SectionResponse 노선의_마지막_구간 = 노선의_구간들.get(1);

        // When
        ExtractableResponse<Response> response = 지하철_구간_제거_요청(신분당선, 노선의_마지막_구간);

        // Then
        노선에_구간이_제거_된_걸_확인_할_수_있다(신분당선, response);
    }

    /**
     * Given 새로운 역을 추가 후
     * Given 지하철 노선에 새로운 구간을 추가 후
     * When 첫 번 째 구간 제거 시
     * Then Exception
     */
    @DisplayName("지하철 노선에 구간을 마지막 하행 종점역만 제거 할 수 있다")
    @Test
    void 지하철_노선에_구간은_마지막_하행_종점역만_제거_할_수_있다() {
        // Given
        지하철_구간_생성(신분당선, 논현역, 신논현역, 10L);

        List<SectionResponse> 노선의_구간들 = 지하철_노선의_구간들_조회_요청(신분당선);
        SectionResponse 노선의_첫번_째_구간 = 노선의_구간들.get(0);

        // When
        ExtractableResponse<Response> actualResponse = 지하철_구간_제거_요청(신분당선, 노선의_첫번_째_구간);

        // Then
        마지막_구간_하행_종점역만_제거_할_수_있다(actualResponse);
    }

    /**
     * When 노선에 구간이 하나인데 제거 시
     * Then Exception
     */
    @DisplayName("지하철 노선에 구간이 하나일 경우 제거 할 수 없다")
    @Test
    void 지하철_노선에_구간이_하나일_경우_제거_할_수_없다() {
        // Given
        List<SectionResponse> 노선의_구간들 = 지하철_노선의_구간들_조회_요청(신분당선);
        SectionResponse 노선의_첫번_째_구간 = 노선의_구간들.get(0);

        // When
        ExtractableResponse<Response> response = 지하철_구간_제거_요청(신분당선, 노선의_첫번_째_구간);

        // Then
        노선에_구간이_하나_일_경우_제거_할_수_없다(response);
    }

    private void 노선에_구간이_하나_일_경우_제거_할_수_없다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 마지막_구간_하행_종점역만_제거_할_수_있다(ExtractableResponse<Response> actualResponse) {
        assertThat(actualResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private SectionResponse 지하철_구간_목록_요청(ExtractableResponse<Response> givenSection) {
        ExtractableResponse<Response> sectionResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("sectionId", givenSection.jsonPath().getLong("id"))
            .when().get("/sections/{sectionId}")
            .then().log().all()
            .extract();

        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return sectionResponse.as(SectionResponse.class);
    }

    private StationResponse 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(params)
            .when().post("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.as(StationResponse.class);
    }

    private ExtractableResponse<Response> 지하철_구간_생성(LineLoadDtoResponse line, StationResponse upStation, StationResponse downStation, Long distance) {
        ExtractableResponse<Response> createResponse = 지하철_구간_생성_요청(line, upStation, downStation, distance);

        return createResponse;
    }

    private void 지하철_구간을_조회_할_수_있다(ExtractableResponse<Response> givenSection, SectionResponse actualSection) {
        assertThat(actualSection).isEqualTo(givenSection.as(SectionResponse.class));
    }

    private List<SectionResponse> 지하철_노선의_구간들_조회_요청(LineLoadDtoResponse lineLoadDtoResponse) {
        ExtractableResponse<Response> response = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", lineLoadDtoResponse.getId())
            .when().get("/lines/{lineId}/sections")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getList(".", SectionResponse.class);
    }

    private void 노선에_구간이_제거_된_걸_확인_할_수_있다(LineLoadDtoResponse 신분당선, ExtractableResponse<Response> lineResponse) {
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        LineLoadDtoResponse response = 지하철_노선_조회(신분당선.getId()).as(LineLoadDtoResponse.class);

        assertThat(response.getStations()).hasSize(2);
        assertThat(response.getStations()).containsExactlyInAnyOrderElementsOf(List.of(강남역, 논현역));
    }

    private ExtractableResponse<Response> 지하철_구간_제거_요청(LineLoadDtoResponse lineDto, SectionResponse sectionResponse) {
        ExtractableResponse<Response> response = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", lineDto.getId())
            .param("sectionId", sectionResponse.getId())
            .when().delete("/lines/{lineId}/sections")
            .then().log().all()
            .extract();

        return response;
    }

    private static ExtractableResponse<Response> 지하철_구간_생성_요청(LineLoadDtoResponse line, StationResponse upStation, StationResponse downStation, Long distance) {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(downStation.getId(), upStation.getId(), distance);

        ExtractableResponse<Response> actualResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", line.getId())
            .body(sectionCreateRequest)
            .when().post("/lines/{lineId}/sections")
            .then().log().all()
            .extract();

        return actualResponse;
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

    private void 지하철_노선을_확인_할_수_있다(SectionResponse sectionResponse) {
        ExtractableResponse<Response> lineResponse = 지하철_노선_조회(sectionResponse.getLineResponse().getId());
        LineLoadDtoResponse lineLoadDtoResponse = lineResponse.as(LineLoadDtoResponse.class);

        assertThat(lineLoadDtoResponse.getStations()).containsExactlyInAnyOrderElementsOf(List.of(강남역, sectionResponse.getUpStation(), sectionResponse.getDownStation()));
    }

    private ExtractableResponse<Response> 지하철_노선_조회(Long lineId) {
        return RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", lineId)
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract();
    }

}
