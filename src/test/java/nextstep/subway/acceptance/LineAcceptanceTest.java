package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.utils.DatabaseCleanup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest extends AcceptanceTest {
    public static final String LINE_CONTROLLER_COMMON_PATH = "/lines";
    public static final LineRequest 신분당선_LineRequest = new LineRequest("신분당선", "bg-red-600");
    public static final LineRequest _2호선_LineRequest = new LineRequest("2호선", "bg-green-600");
    public static final LineRequest 구분당선_LineRequest = new LineRequest("구분당선", "bg-blue-600");

    @Autowired
    DatabaseCleanup databaseCleanup;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(LINE_CONTROLLER_COMMON_PATH)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> ID로_지하철_노선_조회(long 노선_id) {
        return RestAssured
                .given().log().all()
                .when().get(LINE_CONTROLLER_COMMON_PATH + "/" + 노선_id)
                .then().log().all().extract();
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_LineRequest);

        // then
        Assertions.assertThat(response.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.jsonPath().getString("name"))
                .isEqualTo("신분당선");
        Assertions.assertThat(isNull(response.jsonPath().get("id"))
                        || isNull(response.jsonPath().get("createdDate"))
                        || isNull(response.jsonPath().get("modifiedDate")))
                .isFalse();
        Assertions.assertThat(response.jsonPath().getString("color"))
                .isEqualTo("bg-red-600");
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
        지하철_노선_생성_요청(신분당선_LineRequest);
        지하철_노선_생성_요청(_2호선_LineRequest);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get(LINE_CONTROLLER_COMMON_PATH)
                .then().log().all().extract();
        // then

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.jsonPath().getList(".").size()).isEqualTo(2);
        Assertions.assertThat(response.jsonPath().getList("name")).contains("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        long 신분당선_노선_id = 지하철_노선_생성_요청(신분당선_LineRequest).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = ID로_지하철_노선_조회(신분당선_노선_id);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        Assertions.assertThat(response.jsonPath().getLong("id")).isEqualTo(신분당선_노선_id);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // when
        Long 노선_id = 지하철_노선_생성_요청(신분당선_LineRequest).jsonPath().getLong("id");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(구분당선_LineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(LINE_CONTROLLER_COMMON_PATH + "/" + 노선_id)
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 변경된_노선_response = ID로_지하철_노선_조회(노선_id);
        Assertions.assertThat(변경된_노선_response.jsonPath().getString("name")).isEqualTo("구분당선");
        Assertions.assertThat(변경된_노선_response.jsonPath().getString("color")).isEqualTo("bg-blue-600");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
    }
}
