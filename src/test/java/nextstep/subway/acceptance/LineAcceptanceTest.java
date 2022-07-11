package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        createStation(params);
        params.put("name", "역삼역");
        createStation(params);
        params.put("name", "선릉역");
        createStation(params);
    }

    private ExtractableResponse<Response> createStation(Map params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    /**
     * When 지하철노선을 생성하면
     * Then 지하철노선이 생성된다
     * Then 지하철노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "10");

        ExtractableResponse<Response> response = createLine(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(getLines("name").get(0)).isEqualTo("2호선");
    }

    private ExtractableResponse<Response> createLine(Map params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private List<String> getLines(String variable) {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList(variable, String.class);
    }
}
