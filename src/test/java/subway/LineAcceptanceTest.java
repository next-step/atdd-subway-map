package subway;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    private static final String LINE_분당선 = "분당선";
    private static final String STATION_수서역 = "수서역";
    private static final String STATION_복정역 = "복정역";

    private Long 수서역_id;
    private Long 복정역_id;

    @BeforeEach
    void setup() {
        수서역_id = createStation(STATION_수서역);
        복정역_id = createStation(STATION_복정역);
    }

    private Long createStation(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);

        return RestAssured
            .given()
                .log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post(URI.create("/stations"))
            .then()
                .log().all()
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        Map<String, Object> param = new HashMap<>();
        param.put("name", LINE_분당선);
        param.put("color", "yellow");
        param.put("upStationId", 수서역_id);
        param.put("downStationId", 복정역_id);
        param.put("distance", 10);

        // when
        ExtractableResponse<Response> createResponse = RestAssured
            .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
            .when()
                .post(URI.create("/lines"))
            .then()
                .log().all()
            .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = getAllLineNames();
        assertThat(lineNames).contains(LINE_분당선);
    }

    private List<String> getAllLineNames() {
        return RestAssured
            .given()
                .log().all()
            .when()
                .get("/lines")
            .then()
                .log().all()
            .extract()
            .jsonPath().getList("name", String.class);
    }

}
