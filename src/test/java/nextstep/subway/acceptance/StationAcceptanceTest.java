package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given: 지하철 역을 생성한다.
        final String name = "강남역";

        // when: 지하철 역을 생성한다.
        ExtractableResponse<Response> response = createStationRequest(name);

        // then: 지하철역이 정상적으로 생성되었고, 목록에 존재하는지 검증한다.
        assertStatusCode(response, HttpStatus.CREATED);
        assertThat(getStationNamesRequest()).containsAnyOf(name);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given: 2개의 지하철 역을 생성한다.
        final String name1 = "선릉역";
        createStationRequest(name1);

        final String name2 = "역삼역";
        createStationRequest(name2);

        // when: 지하철 역들을 조회한다.
        ExtractableResponse<Response> response = getStationsRequest();

        // then: 지하철 역이 2개인지 검증한다.
        final List<String> names = response.jsonPath()
                .getList("name", String.class);

        assertStatusCode(response, HttpStatus.OK);
        assertThat(names.size()).isEqualTo(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given: 1개의 지하철 역을 생성한다.
        final String name = "잠실역";
        final Long id = createStationRequest(name)
                .jsonPath()
                .getObject("id", Long.class);

        // when: 1개의 지하철 역을 제거한다.
        final ExtractableResponse<Response> response = deleteStationRequest(id);

        // then: 지하철 역이 제거되었는지 검증한다.
        assertStatusCode(response, HttpStatus.NO_CONTENT);
        assertThat(getStationNamesRequest()).doesNotContain(name);
    }

    private ExtractableResponse<Response> createStationRequest(final String name) {
        return RestAssured.given().log().all()
                .body(createStationParams(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private Map<String, Object> createStationParams(final String name) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    private ExtractableResponse<Response> getStationsRequest() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStationRequest(final Long id) {
        return RestAssured
                .given().log().all()
                .when()
                .delete("/stations/{id}", id)
                .then().log().all().extract();
    }

    private List<String> getStationNamesRequest() {
        return getStationsRequest()
                .jsonPath()
                .getList("name", String.class);
    }


    private void assertStatusCode(final ExtractableResponse<Response> response, final HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

}