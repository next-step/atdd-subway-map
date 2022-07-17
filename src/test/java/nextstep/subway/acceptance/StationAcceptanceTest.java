package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tableTruncator")
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseTruncator databaseTruncator;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;
        databaseTruncator.afterPropertiesSet();
    }

    private static Map<String, Object> station1;
    private static Map<String, Object> station2;
    private static Map<String, Object> station3;
    static RestUtil restUtil;

    @BeforeAll
    static void init() {
        restUtil = new RestUtil();

        station1 = new HashMap<>();
        station1.put("name", "마곡역");

        station2 = new HashMap<>();
        station2.put("name", "디지털미디어시티역");

        station3 = new HashMap<>();
        station3.put("name", "마곡나루역");
    }

    @AfterEach
    void tableClear() {
        databaseTruncator.cleanTable();
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
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        //given
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");


        //when
        List<String> names = restUtil.getResponseData("/stations")
                .jsonPath().getList("name", String.class);

        //then
        assertThat(names).containsAnyOf(station1.get("name").toString(), station2.get("name").toString());
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        //given
        Long id = restUtil.createEntityData(station3, "/stations");

        //when
        ExtractableResponse<Response> response = restUtil.deleteEntityDataById("/stations/{id}", id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<String> names = restUtil.getResponseData("/stations")
                .jsonPath().getList("name", String.class);
        assertThat(names).doesNotContain(station3.get("name").toString());


    }
}