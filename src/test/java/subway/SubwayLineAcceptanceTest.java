package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {

    private final static int PORT = 8080;
    private Map<String, Object> SINSA = new HashMap<>();
    private Map<String, Object> GWANGGO = new HashMap<>();
    @BeforeEach
    void setUp() {
        RestAssured.port = PORT;
    }

    @BeforeAll
    void createStation() {
        SINSA.put("name", "신사역");
        long id = RestAssured.given().log().all()
                .body(SINSA)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().body().jsonPath().getLong("id");
        System.out.println("아이디:::::" + id);
        GWANGGO.put("name", "광교중앙역");
        ExtractableResponse<Response> extractGwanggyo =
                RestAssured.given().log().all()
                        .body(GWANGGO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

    }
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO 지하철 노선 생성
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //when
        String name = "신분당선";
        String color = "bg-red-600";
        Long upStationId = 1L;
        Long downStationId = 2L;
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
                .when().post("/line")
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(extract.body().jsonPath().getString("name")).isEqualTo(name);
        assertThat(extract.body().jsonPath().getString("color")).isEqualTo(name);
        assertThat(extract.body().jsonPath().getList("stations").size()).isEqualTo(2);
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
        //given
        long firstLineId = beforeTestCreateLine("5호선", "bg-purple-600", (Long) SINSA.get("id"), (Long) GWANGGO.get("id"), 10);
        long secondLineId = beforeTestCreateLine("4호선", "bg-aqua-600", (Long) SINSA.get("id"), (Long) GWANGGO.get("id"), 10);

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().post("/line/list")
                .then().log().all()
                .extract();
        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        extract.body().jsonPath().get("$.list.*");
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
        //given
        String name = "5호선";
        String color = "bg-purple-600";
        long firstLineId = beforeTestCreateLine(name, color, (Long) SINSA.get("id"), (Long) GWANGGO.get("id"), 10);
        Map<String, String> params = new HashMap<>();

        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/line/{id}", firstLineId)
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extract.body().jsonPath().getString("name")).isEqualTo(name);
        assertThat(extract.body().jsonPath().getString("color")).isEqualTo(color);
        assertThat(extract.body().jsonPath().getList("stations").size()).isEqualTo(2);
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
        //given
        String name = "5호선";
        String color = "bg-purple-600";
        String replaceColor = "bg-red-660";
        long id = beforeTestCreateLine(name, color, (Long) SINSA.get("id"), (Long) GWANGGO.get("id"), 10);
        Map<String, String> params = new HashMap<>();
        params.put("color", replaceColor);
        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/line/{id}", id)
                .then().log().all()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extract.body().jsonPath().getString("color")).isEqualTo(replaceColor);
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
        //given
        String name = "5호선";
        long id = beforeTestCreateLine(name, "bg-purple-600", (Long) SINSA.get("id"), (Long) GWANGGO.get("id"), 10);
        //when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .when().delete("/line/{id}", id)
                .then().log().all()
                .extract();
        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
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
                .when().post("/line")
                .then().log().all()
                .extract();
        return extract.body().jsonPath().getLong("id");
    }
}
