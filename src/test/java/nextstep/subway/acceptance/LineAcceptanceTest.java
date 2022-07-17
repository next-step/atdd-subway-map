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

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tableTruncator")
public class LineAcceptanceTest {
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
    private static Map<String, Object> line2;
    private static Map<String, Object> station1;
    private static Map<String, Object> station2;
    private static Map<String, Object> station3;

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

        line2 = new HashMap<>();
        line2.put("name", "2호선");
        line2.put("color", "bg-green-600");
        line2.put("upStationId", 1);
        line2.put("downStationId", 3);
        line2.put("distance", 10);

        station1 = new HashMap<>();
        station1.put("name", "강남역");

        station2 = new HashMap<>();
        station2.put("name", "양재역");

        station3 = new HashMap<>();
        station3.put("name", "교대역");

    }

    @AfterEach
    void tableClear() {
        databaseTruncator.cleanTable();
    }


    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        //when
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");


        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(line1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.response().getHeader("Location");

    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLines() {
        //given

        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        restUtil.createEntityData(station3, "/stations");
        restUtil.createEntityData(line1, "/lines");
        restUtil.createEntityData(line2, "/lines");

        //when
        List<String> names = restUtil.getResponseData("/lines")
                .jsonPath().getList("name", String.class);

        //then
        assertThat(names).containsAnyOf(line1.get("name").toString(), line2.get("name").toString());
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() {
        //given
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");

        //when
        ExtractableResponse<Response> response = restUtil.getResponseDataById("/lines/{id}", id);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());


    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        //given
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");

        Map<String, String> updateParams = new HashMap<>();

        updateParams.put("name", "구분당선");
        updateParams.put("color", "bg-orange-600");

        //when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(updateParams)
                        .when().put("/lines/{id}", id)
                        .then().log().all()
                        .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());


    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        //given
        restUtil.createEntityData(station1, "/stations");
        restUtil.createEntityData(station2, "/stations");
        Long id = restUtil.createEntityData(line1, "/lines");

        //when
        ExtractableResponse<Response> response = restUtil.deleteEntityDataById("/lines/{id}", id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
