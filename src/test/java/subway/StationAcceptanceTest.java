package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    
    private final static String GET_STATIONS_URL = "/stations";
    private final static String POST_STATIONS_URL = "/stations";
    private final static String DELETE_STATIONS_URL = "/stations/%d";


    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation(){

        // when
        final String 강남역 = "강남역";
        createStation(new TestStationRequest(강남역));

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when()
                            .get(GET_STATIONS_URL)
                        .then()
                            .log().all()
                        .extract()
                            .jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf(강남역);
    }


    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    public void findSubwayStation() {

        // when
        final String 강남역 = "강남역";
        createStation(new TestStationRequest(강남역));
        final String 역삼역 = "역삼역";
        createStation(new TestStationRequest(역삼역));

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when()
                            .get(GET_STATIONS_URL)
                        .then()
                            .log().all()
                        .extract()
                            .jsonPath().getList("name", String.class);

        assertThat(stationNames).containsExactly(강남역, 역삼역);
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    public void deleteSubwayStation() {

        final String 강남역 = "강남역";
        StationResponse gangnamResponse = createStation(new TestStationRequest(강남역));

        RestAssured.given().log().all()
                .when()
                    .delete(String.format(DELETE_STATIONS_URL, gangnamResponse.getId()))
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .log().all();
    }

    @TestFactory
    private StationResponse createStation(TestStationRequest request) {

        return RestAssured.given().log().all()
                    .body(request)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post(POST_STATIONS_URL)
                .then().log().all()
                    .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(StationResponse.class);
    }

    private static class TestStationRequest {
        private String name;

        public TestStationRequest() {
        }

        public TestStationRequest(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


}