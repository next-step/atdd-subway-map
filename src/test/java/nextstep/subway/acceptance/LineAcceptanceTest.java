package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Sql("classpath:station-setup.sql")
    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "분당선");
        params1.put("color", "bg-green-600");
        params1.put("upStationId", "1");
        params1.put("downStationId", "3");
        params1.put("distance", "5");
        registry(params1);

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신분당선");
        params2.put("color", "bg-red-600");
        params2.put("upStationId", "1");
        params2.put("downStationId", "2");
        params2.put("distance", "10");
        registry(params2);

        // then
        final ExtractableResponse<Response> response = getAllLines();
        final List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames).contains("분당선", "신분당선");

        final List<String> list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).contains("[강남역, 선릉역]", "[강남역, 역삼역]");
    }

    private ExtractableResponse<Response> registry(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getAllLines() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("lines")
                .then().log().all()
                .extract();
    }
}
