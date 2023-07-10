package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.read;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SubwayLineAcceptanceTest {
    private final static int PORT = 8080;

    @BeforeEach
    void setUp() {
        RestAssured.port = PORT;
    }
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO 지하철 노선 생성
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        long upStationId = createStation("신사역");
        long downStationId = createStation("광교역");
        //when
        String name = "신분당선";
        String color = "bg-red-600";
        int distance = 10;
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(extract.body().jsonPath().getString("name")).isEqualTo(name);
        assertThat(extract.body().jsonPath().getString("color")).isEqualTo(color);
        assertThat(extract.body().jsonPath().getList("stationResponseList").size()).isEqualTo(2);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO 지하철 노선 목록 조회
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void findLines() {
        long upStationId = createStation("신사역");
        long downStationId = createStation("광교역");
        //given
        long firstLineId = beforeTestCreateLine("5호선", "bg-purple-600", upStationId, downStationId, 10);
        long secondLineId = beforeTestCreateLine("4호선", "bg-aqua-600", upStationId, downStationId, 20);

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
        //then
        System.out.println(extract.jsonPath());
        String prettify = extract.body().jsonPath().prettify();
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat((String) read(prettify, "$.[0].name")).isEqualTo("5호선");
        assertThat((String) read(prettify, "$.[0].stationResponseList[0].name")).isEqualTo("신사역");
        assertThat((String) read(prettify, "$.[0].stationResponseList[1].name")).isEqualTo("광교역");
        assertThat((String) read(prettify, "$.[1].name")).isEqualTo("4호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    // TODO 지하철 노선 조회
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void findLine() {
        long upStationId = createStation("신사역");
        long downStationId = createStation("광교역");
        //given
        String name = "5호선";
        String color = "bg-purple-600";
        long firstLineId = beforeTestCreateLine(name, color, upStationId, downStationId, 10);
        Map<String, String> params = new HashMap<>();

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}", firstLineId)
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extract.body().jsonPath().getString("name")).isEqualTo(name);
        assertThat(extract.body().jsonPath().getString("color")).isEqualTo(color);
        assertThat(extract.body().jsonPath().getList("stationResponseList").size()).isEqualTo(2);
    }
    /**
     *Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO 지하철 노선 수정
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        long upStationId = createStation("신사역");
        long downStationId = createStation("광교역");
        //given
        String name = "5호선";
        String color = "bg-purple-600";
        String replaceColor = "bg-red-660";
        long id = beforeTestCreateLine(name, color, upStationId, downStationId, 10);
        Map<String, String> params = new HashMap<>();
        params.put("color", replaceColor);
        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO 지하철 노선 삭제
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        long upStationId = createStation("신사역");
        long downStationId = createStation("광교역");
        //given
        String name = "5호선";
        long id = beforeTestCreateLine(name, "bg-purple-600", upStationId, downStationId, 10);
        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .extract();
        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    // 지하철역 생성
    private long createStation(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().body().jsonPath().getLong("id");
    }

    private long beforeTestCreateLine(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        return extract.body().jsonPath().getLong("id");
    }
}
