package subway;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.line.LineRequest;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    public static final String 반도선 = "반도선";
    public static final String 제주선 = "제주선";
    public static final String 한라산역 = "한라산역";
    public static final String 백두산역 = "백두산역";
    public static final String 서귀포역 = "서귀포역";

    @BeforeEach
    void setUp() {
        setUpStation(한라산역);
        setUpStation(백두산역);
        setUpStation(서귀포역);
    }

    /**
     * 지하철노선 수정
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 노선_수정_테스트() {
        // given
        LineRequest lineRequests =
            LineRequest.of(제주선, "green", 1, 3, 10);
        long lineId = LineAcceptanceTest.노선_생성(lineRequests).jsonPath().getLong("id");

        // when
        LineRequest modifyLine = LineRequest.of(반도선, "red", 1, 2, 10);
        ExtractableResponse<Response> response = 노선_수정(lineId, modifyLine);

        // then
        assertThat(response.jsonPath().getString("name")).isEqualTo(반도선);
        assertThat(response.jsonPath().getString("name")).isNotEqualTo(제주선);
    }

    /**
     * 지하철노선 조회
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 노선_조회_테스트() {
        // given
        LineRequest lineRequests =
            LineRequest.of(제주선, "green", 1, 3, 10);
        long lineId = LineAcceptanceTest.노선_생성(lineRequests).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 노선_조회(lineId);

        // then
        assertThat(response.jsonPath().getString("name")).isEqualTo(제주선);

    }

    /**
     * 지하철 노선 목록 조회
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void 노선_목록_조회_테스트() {
        // given
        List<LineRequest> lineRequests =
            List.of(
                LineRequest.of(반도선, "red", 1, 2, 10),
                LineRequest.of(제주선, "green", 1, 3, 10)
            );
        lineRequests.forEach(LineAcceptanceTest::노선_생성);

        // when
        List<String> lines = 노선_목록_조회();

        // then
        assertThat(lines).containsExactly(반도선, 제주선);
        assertThat(lines.size()).isEqualTo(lineRequests.size());
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void 노선_생성_테스트() {

        LineRequest request = LineRequest.of(반도선, "red", 1, 2, 10);

        // when
        ExtractableResponse<Response> response = 노선_생성(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = 노선_목록_조회();
        assertThat(lineNames).contains(반도선);
    }

    private static ExtractableResponse<Response> 노선_생성(LineRequest request) {
        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
    }

    private static List<String> 노선_목록_조회() {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/lines")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    }

    private static void setUpStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 노선_수정(long id, LineRequest request) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .put("/lines/{id}", id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 노선_조회(long id) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/lines/{id}", id)
            .then().log().all()
            .extract();
    }
}
