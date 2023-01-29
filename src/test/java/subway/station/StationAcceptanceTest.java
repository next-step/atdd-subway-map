package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void Should_지하철역을_생성하면_Then_지하철역이_생성된다() {
        // when
        var 강남역_request = new StationRequest() {{
            setName("강남역");
        }};
        ExtractableResponse<Response> 강남역_response = 지하철역을_생성한다(강남역_request);

        // then
        지하철역이_정상적으로_생성(강남역_response);

        // then
        ExtractableResponse<Response> stationsResponse = 지하철역을_조회한다();
        List<String> stationNames = 지하철역이_정상적으로_조회(stationsResponse);
        assertThat(stationNames).containsExactlyInAnyOrderElementsOf(List.of(강남역_request.getName()));
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void Should_지하철역을_생성하고_When_지하철역을_조회하면_Then_지하철역이_조회된다() {
        //given
        var 강남역_request = new StationRequest() {{
            setName("강남역");
        }};
        ExtractableResponse<Response> 강남역_response = 지하철역을_생성한다(강남역_request);
        지하철역이_정상적으로_생성(강남역_response);
        var 역삼역_request = new StationRequest() {{
            setName("역삼역");
        }};
        ExtractableResponse<Response> 역삼역_response = 지하철역을_생성한다(역삼역_request);
        지하철역이_정상적으로_생성(역삼역_response);

        // when
        ExtractableResponse<Response> stationsResponse = 지하철역을_조회한다();

        // then
        List<String> stationNames = 지하철역이_정상적으로_조회(stationsResponse);
        assertThat(stationNames).containsExactlyInAnyOrderElementsOf(List.of(강남역_request.getName(), 역삼역_request.getName()));
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void Should_지하철역을_삭제하면_Then_지하철역이_삭제된다() {
        // given
        var 강남역_request = new StationRequest() {{
            setName("강남역");
        }};
        ExtractableResponse<Response> 강남역_response = 지하철역을_생성한다(강남역_request);
        지하철역이_정상적으로_생성(강남역_response);

        var 강남역Id = 강남역_response.response().as(StationResponse.class).getId();

        //when
        ExtractableResponse<Response> response = 지하철역을_삭제한다(강남역Id);
        지하철역이_정상적으로_삭제(response);

        // then
        ExtractableResponse<Response> stationsResponse = 지하철역을_조회한다();
        List<String> stationNames = 지하철역이_정상적으로_조회(stationsResponse);
        assertThat(stationNames).doesNotContain(강남역_request.getName());
    }

    public static ExtractableResponse<Response> 지하철역을_생성한다(StationRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역을_조회한다() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역을_삭제한다(Long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .extract();
    }

    public static void 지하철역이_정상적으로_생성(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private List<String> 지하철역이_정상적으로_조회(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList("name", String.class);
    }

    private void 지하철역이_정상적으로_삭제(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
