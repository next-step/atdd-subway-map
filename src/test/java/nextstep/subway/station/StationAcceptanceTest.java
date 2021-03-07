package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        Map<String, String> params = 지하철역_등록되어_있음("강남역");

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(params);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        Map<String, String> params = 지하철역_등록되어_있음("강남역");
        지하철역_생성_요청(params);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(params);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        Map<String, String> params1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createResponse1 = 지하철역_생성_요청(params1);

        Map<String, String> params2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createResponse2 = 지하철역_생성_요청(params2);

        // when
        ExtractableResponse<Response> response = 지하철역_조회_요청();

        // then
        지하철역_목록_조회됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(params);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(createResponse);

        // then
        지하철역_제거됨(response);
    }

    private Map<String, String> 지하철역_등록되어_있음(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        return params;
    }

    private ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private void 지하철역_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }

    private void 지하철역_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철역_목록_포함됨(ExtractableResponse<Response> readAllLinesResponse,
                             List<ExtractableResponse<Response>> createLineResponses) {
        List<Long> resultLineIds = readAllLinesResponse.jsonPath().getList(".", StationResponse.class)
                .stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createLineResponses
                .stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsExactlyInAnyOrderElementsOf(expectedLineIds);
    }

    private ExtractableResponse<Response> 지하철역_제거_요청(ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private void 지하철역_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
