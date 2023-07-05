package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.helper.SubwayStationHelper;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철역 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    private final String STATION_API_URL = "/stations";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        final String DEFAULT_STATION_NAME = "강남역";
        Map<String, String> parameter = new HashMap<>();
        parameter.put("name", DEFAULT_STATION_NAME);

        ExtractableResponse<Response> response = RestAssured
                        .given().log().all()
                            .body(parameter)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                            .post(STATION_API_URL)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = RestAssured
                        .given().log().all()
                        .when()
                            .get(STATION_API_URL)
                        .then().log().all()
                        .extract()
                            .jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf(DEFAULT_STATION_NAME);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        // given
        Stream.of("구디역", "봉천역")
                .forEach(SubwayStationHelper::지하철_역_생성_요청);

        // when
        List<String> response = RestAssured
                        .given().log().all()
                        .when()
                            .get(STATION_API_URL)
                        .then().log().all()
                        .extract()
                            .jsonPath().getList("name", String.class);

        // then
        Assertions.assertThat(response).containsOnly("구디역", "봉천역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철 역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createStationApiResponse = SubwayStationHelper.지하철_역_생성_요청("봉천역");
        String createStationApiResponseUrl = createStationApiResponse
                .response().getHeaders().getValue("Location");

        // when
        ExtractableResponse<Response> deleteStationApiResponse = RestAssured
                .given().log().all()
                .when().log().all()
                    .delete(createStationApiResponseUrl)
                .then()
                    .extract();

        // then
        assertThat(deleteStationApiResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}