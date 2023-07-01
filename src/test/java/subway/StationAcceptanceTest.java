package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    private final String CREATE_STATION_API_URL = "/stations";
    private final String SHOW_STATIONS_API_URL = "/stations";

    static Stream stationNames() {
        return Stream.of("강남역");
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @ParameterizedTest
    @MethodSource("stationNames")
    void createStation(String stationName) {
        // when
        Map<String, String> parameter = new HashMap<>();

        parameter.put("name", stationName);

        ExtractableResponse<Response> response =
                RestAssured
                        .given()
                            .log()
                                .all()
                            .body(parameter)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                            .post(CREATE_STATION_API_URL)
                        .then()
                            .log()
                                .all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured
                        .given()
                            .log()
                                .all()
                        .when()
                            .get(SHOW_STATIONS_API_URL)
                        .then()
                            .log()
                                .all()
                        .extract()
                            .jsonPath()
                                .getList("name", String.class);

        assertThat(stationNames).containsAnyOf(stationName);
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
        List<String> stationNames = Arrays.asList("구디역", "봉천역");
        for (String stationName : stationNames) {
            createStation(stationName);
        }

        // when
        List<String> response = RestAssured
                        .given()
                            .log()
                                .all()
                        .when()
                            .get(SHOW_STATIONS_API_URL)
                        .then()
                            .log()
                                .all()
                        .extract()
                            .jsonPath()
                                .getList("name", String.class);

        // then
        Assertions.assertThat(response).contains("구디역", "봉천역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
}