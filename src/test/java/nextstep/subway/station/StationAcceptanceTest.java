package nextstep.subway.station;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private Map<String, String> 강남역;
    private Map<String, String> 역삼역;

    @BeforeEach
    void before() {
        강남역 = 지하철역_생성("강남역");
        역삼역 = 지하철역_생성("역삼역");
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> 강남역_응답 = 지하철역_생성_요청(강남역);
        ExtractableResponse<Response> 역삼역_응답 = 지하철역_생성_요청(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_조회_요청();

        // then
        지하철역_조회_응답됨(강남역_응답, 역삼역_응답, response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> 강남역_응답 = 지하철역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(강남역_응답);

        // then
        지하철역_제거됨(response);
    }

    private Map<String, String> 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
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

    private void 지하철역_조회_응답됨(
        ExtractableResponse<Response> 강남역_응답,
        ExtractableResponse<Response> 역삼역_응답,
        ExtractableResponse<Response> response) {

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> expectedLineIds = Stream.of(강남역_응답, 역삼역_응답)
            .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
            .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> 지하철역_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
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
