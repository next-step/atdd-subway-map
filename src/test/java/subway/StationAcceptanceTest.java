package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.fixture.StationSteps;
import subway.station.StationResponse;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StationAcceptanceTest {
    
    private final static String GET_STATIONS_URL = "/stations";
    private final static String POST_STATIONS_URL = "/stations";
    private final static String DELETE_STATIONS_URL = "/stations/%d";
    //@Autowired
    //private EntityManager em;

    @BeforeEach
    void setUp() {
        //em.createNativeQuery("TRUNCATE TABLE Station").executeUpdate();
        //em.flush();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation(){

        // when
        StationResponse 강남역 = StationSteps.createStation("강남역");

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when()
                            .get(GET_STATIONS_URL)
                        .then()
                            .log().all()
                        .extract()
                            .jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf(강남역.getName());
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
        StationResponse 강남역 = StationSteps.createStation("강남역");
        StationResponse 역삼역 = StationSteps.createStation("역삼역");

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when()
                            .get(GET_STATIONS_URL)
                        .then()
                            .log().all()
                        .extract()
                            .jsonPath().getList("name", String.class);

        assertThat(stationNames).containsExactly(강남역.getName(), 역삼역.getName());
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    public void deleteSubwayStation() {

        StationResponse 강남역 = StationSteps.createStation("강남역");

        // when
        RestAssured.given().log().all()
                .when()
                    .delete(String.format(DELETE_STATIONS_URL, 강남역.getId()))
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .log().all();

        // then
        List<String> acutal = RestAssured.given()
                .when()
                .get(GET_STATIONS_URL)
                .then().log().all()
                .extract()
                .jsonPath().getList("name", String.class);

        assertThat(acutal).isEqualTo(Collections.emptyList());
    }

}