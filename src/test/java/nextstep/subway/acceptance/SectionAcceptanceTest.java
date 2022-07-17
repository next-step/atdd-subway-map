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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tableTruncator")
public class SectionAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseTruncator databaseTruncator;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;
        databaseTruncator.afterPropertiesSet();
    }

    private static Map<String, Object> line1;
    private static Map<String, Object> section1;
    private static Map<String, Object> section2;
    private static Map<String, Object> section3;
    private static Map<String, Object> station1;
    private static Map<String, Object> station2;
    private static Map<String, Object> station3;
    private static Map<String, Object> station4;

    static RestUtil restUtil;

    @BeforeAll
    static void init() {
        restUtil = new RestUtil();
        line1 = new HashMap<>();
        line1.put("name", "신분당선");
        line1.put("color", "bg-red-600");
        line1.put("upStationId", 1);
        line1.put("downStationId", 2);
        line1.put("distance", 10);

        section1 = new HashMap<>();
        section1.put("upStationId", 2);
        section1.put("downStationId", 3);
        section1.put("distance", 10);

        section2 = new HashMap<>();
        section2.put("upStationId", 3);
        section2.put("downStationId", 4);
        section2.put("distance", 10);

        section3 = new HashMap<>();
        section3.put("upStationId", 2);
        section3.put("downStationId", 1);
        section3.put("distance", 10);

        station1 = new HashMap<>();
        station1.put("name", "강남역");

        station2 = new HashMap<>();
        station2.put("name", "양재역");

        station3 = new HashMap<>();
        station3.put("name", "교대역");

        station4 = new HashMap<>();
        station4.put("name", "신촌역");


    }

    @AfterEach
    void tableClear() {
        databaseTruncator.cleanTable();
    }

    /*
     * When 지하철 노선에 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시 등록한 지하철 구간을 확인할 수 있다
     * */
    @DisplayName("지하철 구간을 등록한다")
    @Test
    void registerSection() {
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        restUtil.createEntityData(station3, "/stations");
        restUtil.createEntityData(station4, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(section1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines/" + id + "/sections")
                        .then().log().all()
                        .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 =
                RestAssured.given().log().all()
                        .body(section2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines/" + id + "/sections")
                        .then().log().all()
                        .extract();
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());


        ExtractableResponse<Response> lineResponse = restUtil.getResponseDataById("/lines/{id}", id);

        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());


    }

    @DisplayName("등록된 노선의 하행과 새로 등록할 구간의 상행이 맞지 않으면 등록할 수 없다.")
    @Test
    void matchStation() {
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        restUtil.createEntityData(station3, "/stations");
        restUtil.createEntityData(station4, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(section2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines/" + id + "/sections")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("등록할 구간의 하행은 이미 등록되어 있으면 안된다")
    @Test
    void checkDuplicate(){
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(section3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines/" + id + "/sections")
                        .then().log().all()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /*
     * Given 지하철 노선에 지하철 구간을 등록하고
     * when 등록한 지하설 구간을 삭제하면
     * Then 해당 지하철 구간은 삭제된다
     * */
    @DisplayName("지하철 구간을 삭제한다")
    @Test
    void deleteSection() {
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        restUtil.createEntityData(station3, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");
        restUtil.registEntityData(section1, "/lines/" + id + "/sections");

        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .given().queryParam("stationId", section1.get("upStationId"))
                .when().delete("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> lineResponse = restUtil.getResponseDataById("/lines/{id}", id);
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @DisplayName("한개의 구간만 지닌 노선의 구간을 삭제할 수 없다.")
    @Test
    void canNotDeleteOneSection() {
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");

        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .given().queryParam("stationId", line1.get("upStationId"))
                .when().delete("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());


    }

    @DisplayName("마지막 구간이 아닌 것은 제거 할 수 없다.")
    @Test
    void deleteOnlyDownSation() {
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        restUtil.createEntityData(station3, "/stations");
        restUtil.createEntityData(station4, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");
        restUtil.registEntityData(section1, "/lines/" + id + "/sections");
        restUtil.registEntityData(section2, "/lines/" + id + "/sections");

        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .given().queryParam("stationId", section1.get("upStationId"))
                .when().delete("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

}

