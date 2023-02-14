package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.dto.LinePatchResponse;
import subway.dto.LineRequest;
import subway.utils.DatabaseCleanup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    private static final int GANG_NAM_STATION = 1;
    private static final int YANG_JAE_STATION = 2;
    private static final int SEO_HYEON_STATION = 3;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        StationAcceptanceTest.createStation("강남역");
        StationAcceptanceTest.createStation("양재역");
        StationAcceptanceTest.createStation("서현역");
    }

    /*
    * 지하철 노선 생성
    * when 지하철 노선을 생성하면
    * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
    * */

    @DisplayName("지하철노선 생성 인수 테스트")
    @Test
    void 지하철_노선_생성_인수_테스트() {
        //when
        createLine("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);

        List<String> lineList = getLines();

        assertThat(lineList).containsAnyOf("신분당선");
    }


    public ExtractableResponse<Response> createLine(String name, String color, long upStationId, long downStationId, int distance) {
        LineRequest param = new LineRequest(name, color, upStationId, downStationId, distance);

        return RestAssured.given().log().all()
                .body(param).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public List<String> getLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all().extract().jsonPath().getList("name", String.class);
    }


    /*
    * 지하철 노선 목록 조회
    * given 2개의 지하철 노선을 생성하고
    * when 지하철 노선 목록을 조회하면
    * then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    * */

    @DisplayName("지하철노선 목록 조회 인수테스트")
    @Test
    void 지하철_노선_목록_조회_인수_테스트() {
        //given
        createLine("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);
        createLine("분당선", "bg-green-600", GANG_NAM_STATION, SEO_HYEON_STATION, 10);

        //when
        List<String> lineList = getLines();

        //then
        assertThat(lineList).containsExactly("신분당선", "분당선");
    }

    /*
    * given 지하철 노선을 생성하고
    * when 생성한 지하철 노선을 조회하면
    * then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    * */

    @DisplayName("지하철노선 조회 인수테스트")
    @Test
    void 지하철_노선_조회_인수_테스트() {
        //given
        ExtractableResponse<Response> response = createLine("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);
        long id = response.jsonPath().getLong("id");
        //when
        List<String> line = getLine(id).getList("stations.name", String.class);
        //then
        assertAll(
                () -> assertThat(line).containsAnyOf("강남역"),
                () -> assertThat(getJson(id).get(0)).isEqualTo("신분당선"),
                () -> assertThat(getJson(id).get(1)).isEqualTo("bg-red-600")
        );
    }

    public JsonPath getLine(Long id) {
        return RestAssured.given().log().all()
                .pathParam("id", id)
                .when().get("/lines/{id}")
                .then().log().all().extract().jsonPath();
    }


    /*
    * 지하철노선 수정
    * given 지하철 노선을 생성하고
    * when 생성한 지하철 노선을 수정하면
    * then 해당 지하철 노선 정보는 수정된다.
    * */

    @DisplayName("지하철노선 수정 인수테스트")
    @Test
    void 지하철_노선_수정_인수_테스트() {
        //given
        ExtractableResponse<Response> createResponse
                = createLine("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);
        long id = createResponse.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> patchResponse = patchLine("다른분당선", "bg-red-600");
        assertThat(patchResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        assertAll(
                () -> assertThat(getJson(id).get(0)).isEqualTo("다른분당선"),
                () -> assertThat(getJson(id).get(1)).isEqualTo("bg-red-600")
        );
    }

    private List<String> getJson(Long id) {
        return List.of(getLine(id).getString("name"), getLine(id).getString("color"));
    }

    public ExtractableResponse<Response> patchLine(String name, String color) {
        LinePatchResponse patchResponse = new LinePatchResponse(name, color);

       return RestAssured.given().header("Content-type", "application/json")
               .log().all()
               .and()
               .body(patchResponse)
               .when().patch("/lines/1")
               .then().log().all().extract();
    }

    /*
    * Given 지하철 노선을 생성하고
      When 생성한 지하철 노선을 삭제하면
      Then 해당 지하철 노선 정보는 삭제된다
    * */

    @DisplayName("지하철노선 삭제 인수테스트")
    @Test
    void 지하철_노선_삭제_인수_테스트() {

        //given
        createLine("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);

        //when
        ExtractableResponse<Response> response = deleteLine("/lines/1");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public ExtractableResponse<Response> deleteLine(String path) {
        return RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all().extract();
    }


}
