package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseCleanup;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest extends AcceptanceTest {
    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "bg-red-600");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(final String name, final String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given().log().all()
                          .body(params)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines")
                          .then().log().all()
                          .extract();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        지하철_노선_생성_요청("2호선", "bg-red-600");
        지하철_노선_생성_요청("3호선", "bg-black-600");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/lines")
                                                            .then().log().all()
                                                            .extract();
        List<String> lineNames = response.jsonPath().getList("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).containsExactly("2호선", "3호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        지하철_노선_생성_요청("2호선", "bg-red-600");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .get("/lines/1")
                                                            .then().log().all()
                                                            .extract();
        String lineName = response.jsonPath().get("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineName).isEqualTo("2호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        지하철_노선_생성_요청("2호선", "bg-red-600");

        Map<String, String> params = new HashMap<>();
        params.put("name", "3호선");
        params.put("color", "bg-blue-600");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(params)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when()
                                                            .put("/lines/1")
                                                            .then().log().all()
                                                            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        지하철_노선_생성_요청("2호선", "bg-red-600");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .when()
                                                            .delete("/lines/1")
                                                            .then().log().all()
                                                            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
