package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void cleanupDatabase() {
        databaseCleanup.execute();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        Map<String, String> params = createStationInput("강남역");

        // when
        ExtractableResponse<Response> response = createStationHelper(params);

        // then
        assertCreateStationSuccess(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        Map<String, String> params = createStationInput("강남역");
        createStationHelper(params).as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = createStationHelper(params);

        // then
        assertDuplicateStationStatusFail(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        Map<String, String> params1 = this.createStationInput("강남역");
        ExtractableResponse<Response> createResponse1 = this.createStationHelper(params1);

        Map<String, String> params2 = this.createStationInput("역삼역");
        ExtractableResponse<Response> createResponse2 = this.createStationHelper(params2);

        // when
        ExtractableResponse<Response> response = this.getStationsHelper();

        // then
        assertRetrieveStationStatusSuccess(response);
        assertRetrieveStationDetailSuccess(response, createResponse1, createResponse2);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = this.createStationInput("강남역");
        ExtractableResponse<Response> createResponse = this.createStationHelper(params);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = this.deleteStationHelper(uri);

        // then
        assertDeleteStationStatusSuccess(response);
    }

    private Map<String, String> createStationInput(final String stationName) {
        return new HashMap<String, String>() {
            {
                put("name", stationName);
            }
        };
    }

    private ExtractableResponse<Response> createStationHelper(Map params) {
        return RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/stations")
                .then().log().all()
                    .extract();
    }

    private ExtractableResponse<Response> deleteStationHelper(final String uri) {
        return  RestAssured.given().log().all()
                .when()
                    .delete(uri)
                .then().log().all()
                .   extract();
    }

    private ExtractableResponse<Response> getStationsHelper(){
        return RestAssured.given().log().all()
                .when()
                    .get("/stations")
                .then().log().all()
                    .extract();
    }

    private void assertCreateStationSuccess(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void assertDuplicateStationStatusFail(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void assertRetrieveStationStatusSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void assertDeleteStationStatusSuccess(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void assertRetrieveStationDetailSuccess(
            ExtractableResponse<Response> resultResponse,
            ExtractableResponse<Response>... args) {
        List<Long> expectedLineIds = Arrays.asList(args).stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = resultResponse.jsonPath().getList(".", StationResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
