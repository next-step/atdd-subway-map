package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        databaseCleanup.execute();

        StationAcceptanceTest.지하철역_등록_요청("강남역");
        StationAcceptanceTest.지하철역_등록_요청("역삼역");
        StationAcceptanceTest.지하철역_등록_요청("판교역");
    }

    /**
     * Given    지하철 노선을 생성하면
     * When     지하철 노선 목록을 조회 시 생성한 노선을 찾을 수 있다.
     */
    @Test
    @DisplayName("지하철 노선 생성한다.")
    void createStationLine() {
        // given
        노선_생성_요청("2호선", "bg-green-600", 1L, 2L, 10);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회_요청();
        List<String> 노선_이름_목록 = response.jsonPath().getList("name", String.class);

        // then
        assertThat(노선_이름_목록).contains("2호선");
    }

    /**
     * Given    2개의 지하철 노선을 생성하고
     * When     지하철 노선 목록을 조회하면
     * Then     지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철노선 목록을 조회한다.")
    void getLines() {
        // given
        노선_생성_요청("2호선", "bg-green-600", 1L, 2L, 10);
        노선_생성_요청("신분당선", "bg-red-600", 1L, 3L, 20);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회_요청();
        List<String> 노선_이름_목록 = response.jsonPath().getList("name", String.class);

        // then
        assertThat(노선_이름_목록).containsOnly("2호선", "신분당선");
    }


    /**
     * Given    지하철 노선을 생성하고
     * When     생성한 지하철 노선을 조회하면
     * Then     생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철노선을 조회한다.")
    void getLine() {
        // given
        ExtractableResponse<Response> savedResponse = 노선_생성_요청("신분당선", "bg-red-600", 1L, 3L, 20);
        Long 신분당선_ID = savedResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(신분당선_ID);
        String 조회한_노선_이름 = response.jsonPath().getString("name");

        // then
        assertThat(조회한_노선_이름).isEqualTo("신분당선");
    }

    /**
     * Given    지하철 노선을 생성하고
     * When     생성한 지하철 노선을 수정하면
     * Then     해당 지하철 노선 정보는 수정된다.
     */
    @Test
    @DisplayName("지하철노선을 수정한다.")
    void modifyLine() {
        // given
        ExtractableResponse<Response> savedResponse = 노선_생성_요청("신분당선", "bg-red-600", 1L, 3L, 20);
        Long 신분당선_ID = savedResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 노선_수정_요청(신분당선_ID, "2호선", "bg-green-600");
        String 조회한_노선_이름 = response.jsonPath().getString("name");
        String 조회한_노선_색상 = response.jsonPath().getString("color");

        // then
        assertThat(조회한_노선_이름).isEqualTo("2호선");
        assertThat(조회한_노선_색상).isEqualTo("bg-green-600");
    }

    private ExtractableResponse<Response> 노선_생성_요청(String name, String color, long upStationId, long downStationId, int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private ExtractableResponse<Response> 노선_목록_조회_요청() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private ExtractableResponse<Response> 노선_조회_요청(Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private ExtractableResponse<Response> 노선_수정_요청(Long id, String name, String color) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }
}
