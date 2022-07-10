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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/station.sql"})
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역 생성을 요청하면
     * Then 지하철역이 생성된다.
     * @param stationName
     */
    public ExtractableResponse<Response> createStation(String stationName) {

        final String url = "/stations";
        final Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(url)
                .then().log().all()
                .extract();

        return response;
    }

    public ExtractableResponse<Response> gets() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = createStation("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = gets().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("지하철역 생성 시 같은 이름을 가질 경우 예외")
    @Test
    void throwsIfEqualsStationName() {

        //given
        ExtractableResponse<Response> createResponseOne = createStation("강남역");
        assertThat(createResponseOne.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> createResponseTwo = createStation("강남역");

        //then
        assertThat(createResponseTwo.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStations() {

        //given
        final String 강남역 = "강남역";
        final String 교대역 = "교대역";

        ExtractableResponse<Response> createResponseOne = createStation(강남역);
        assertThat(createResponseOne.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> createResponseTwo = createStation(교대역);
        assertThat(createResponseTwo.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<String> names = gets().jsonPath().getList("name", String.class);

        //then
        assertThat(names).contains(강남역, 교대역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 제거")
    @Test
    void removeStation() {

        //given
        String 강남역 = "강남역";
        ExtractableResponse<Response> createResponse = createStation(강남역);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        long id = createResponse.body().jsonPath().getLong("id");

        //when
        RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();

        //then
        List<String> names = gets().jsonPath().getList("name", String.class);

        assertThat(names).doesNotContain(강남역);
    }


}