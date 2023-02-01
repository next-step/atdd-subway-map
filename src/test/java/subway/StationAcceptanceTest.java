package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
public class StationAcceptanceTest extends Acceptance {
    private static final String URI_DELIMITER = "/";
    private static final int STATION_ID_INDEX = 2;
    private static final String STATION_NAME = "name";
    private static final String STATION_ID = "id";
    private static final String LOCATION = "Location";
    private static final String STATION_PATH = "/stations";
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철역_조회_요청().jsonPath().getList(STATION_NAME, String.class);
        assertThat(stationNames).containsAnyOf(강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철역_생성_요청(강남역);
        ExtractableResponse<Response> createResponse2 = 지하철역_생성_요청(역삼역);
        List<Long> expectedStationIds = getCreateStationIds(Arrays.asList(createResponse1, createResponse2));

        // when
        ExtractableResponse<Response> getResponse = 지하철역_조회_요청();
        List<Long> stationIds = getResponse.jsonPath().getList(STATION_ID, Long.class);

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationIds).containsAll(expectedStationIds);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철역_삭제_요청(createResponse);

        // then
        List<String> stationNames = 지하철역_조회_요청().jsonPath().getList(STATION_NAME, String.class);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(stationNames).doesNotContain(강남역).isEmpty();
    }

    private ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put(STATION_NAME, name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(STATION_PATH)
                .then().log().all()
                .extract();
    }

    private List<Long> getCreateStationIds(List<ExtractableResponse<Response>> createResponses) {
        return createResponses.stream()
                .map(response -> Long.parseLong(response.header(LOCATION).split(URI_DELIMITER)[STATION_ID_INDEX]))
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(STATION_PATH)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION);
        return RestAssured.given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }
}
