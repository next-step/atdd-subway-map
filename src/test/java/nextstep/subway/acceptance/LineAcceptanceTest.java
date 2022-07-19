package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.util.Arrays;
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
        databaseTruncator.cleanTable();
    }

    private static List<Map<String, Object>> lines;
    private static List<Map<String, Object>> stations;

    static RestUtil restUtil;
    static SubwayTestHelper testHelper;

    @BeforeAll
    static void init() {
        restUtil = new RestUtil();
        testHelper = new SubwayTestHelper();

        lines = testHelper.makeLineList(List.of("신분당선", "bg-red-600", 1L, 2L, 10L),
                List.of("2호선", "bg-green-600", 1L, 3L, 10L));
        stations = testHelper.makeStationList("강남역","양재역", "교대역");
    }

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        //when
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1));


        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(lines.get(0))
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
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1), stations.get(2));
        testHelper.makeGivenCondition("/lines", HttpStatus.CREATED.value(), lines.get(0), lines.get(1));

        //when
        List<String> names = restUtil.getResponseData("/lines")
                .jsonPath().getList("name", String.class);

        //then
        assertThat(names).containsAnyOf(lines.get(0).get("name").toString(), lines.get(1).get("name").toString());
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() {
        //given
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), lines.get(0));

        //when
        ExtractableResponse<Response> response = restUtil.getResponseDataById("/lines/{id}", id);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());


    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        //given
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), lines.get(0));

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
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), lines.get(0));

        //when
        ExtractableResponse<Response> response = restUtil.deleteEntityDataById("/lines/{id}", id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
