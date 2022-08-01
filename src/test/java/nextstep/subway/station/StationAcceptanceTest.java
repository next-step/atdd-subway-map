package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final ExtractableResponse<Response> creationResponse = 지하철역_생성(GANGNAM_STATION);

        지하철역이_생성됨(creationResponse);

        지하철역명_조회(GANGNAM_STATION);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStation() {
        지하철역_생성(GANGNAM_STATION);
        지하철역_생성(SINDORIM_STATION);

        final ExtractableResponse<Response> stationsResponse = 지하철역_조회();

        지하철역이_조회됨(stationsResponse);
        지하철역명_조회(GANGNAM_STATION, SINDORIM_STATION);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        final ExtractableResponse<Response> creationResponse = 지하철역_생성(GANGNAM_STATION);

        final ExtractableResponse<Response> deletionResponse = 지하철역_삭제(creationResponse);

        지하철역이_삭제됨(deletionResponse);
        지하철역명_조회();
    }

    public static ExtractableResponse<Response> 지하철역_생성(final String stationName) {
        return RestAssured.given().log().all()
                .body(Map.of(KEY_NAME, stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private void 지하철역이_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 지하철역이_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철역명_조회(final String... station) {
        final List<String> stations = List.of(station);
        assertThat(stations).isEqualTo(지하철역명_조회());
    }

    private List<String> 지하철역명_조회(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(KEY_NAME, String.class);
    }

    private List<String> 지하철역명_조회() {
        final ExtractableResponse<Response> stationResponse = 지하철역_조회();
        return 지하철역명_조회(stationResponse);
    }

    private ExtractableResponse<Response> 지하철역_삭제(final ExtractableResponse<Response> response) {
        final Long id = response.jsonPath().getObject(KEY_ID, Long.class);
        return 지하철역_삭제(id);
    }

    private ExtractableResponse<Response> 지하철역_삭제(final Long id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }

    private void 지하철역이_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
