package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {

        Map<String, String> params = new HashMap<>();
        params.put("name", "구로역");

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations");

        params.put("name", "부천역");
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations");

        ExtractableResponse<Response> response = createLine(new Line("1호선", "bg-blue-600", 1L, 2L, 10));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

//        List<String> lineNames = RestAssured.given().log().all()
//                .when().get("/lines")
//                .then().log().all()
//                .extract().jsonPath().getList("name", String.class);
//
//        assertThat(lineNames.contains("1호선")).isEqualTo(true);
    }


    // 지하철노선 생성
    private ExtractableResponse<Response> createLine(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("upStationId", line.getUpStationId());
        params.put("downStationId", line.getDownStationId());
        params.put("distance", line.getDistance());

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }



}
