package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.AcceptanceTestHelper.*;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/stations.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    private static final LineRequest 신분당선 =
            LineRequest.of("신분당선", "bg-red-600", 1L, 2L, 10);
    private static final LineRequest 수인분당선 =
            LineRequest.of("수인분당선", "bg-yellow-600", 2L, 3L, 8);
    private static final LineRequest 다른분당선 = LineRequest.of("다른분당선", "bg-green-600");

    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        //when
        ExtractableResponse<Response> response = post("/lines", 신분당선);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).contains("신분당선");
    }

    @DisplayName("두 개의 지하철 노선을 생성하고 지하철 노선 목록 조회 한다.")
    @Test
    void findLines() {
        //given
        post("/lines", 신분당선);
        post("/lines", 수인분당선);

        //when
        ExtractableResponse<Response> response = get("/lines");
        List<String> lineNames = response.jsonPath().getList("name");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).contains("신분당선", "수인분당선");
    }

    @DisplayName("id에 해당하는 지하철 노선을 조회한다.")
    @Test
    void findOneLine() {
        //given
        post("/lines", 신분당선);

        //when
        ExtractableResponse<Response> response = get("lines/{id}", 1L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).contains("신분당선");
    }


    @DisplayName("id에 해당하는 지하철 노선을 수정한다")
    @Test
    void updateOneLine() {
        //given
        post("/lines", 신분당선);

        //when
        ExtractableResponse<Response> response = put("/lines/{id}", 1L, 다른분당선);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).contains("다른분당선");
    }

    @DisplayName("id에 해당하는 지하철 노선을 삭제한다.")
    @Test
    void deleteOneLine() {
        //given
        post("/lines", 신분당선);

        //when
        ExtractableResponse<Response> response = delete("/lines/{id}", 1L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> get(String url) {
        return given().log().all()
                .when().get(url)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> get(String url, Long pathParams) {
        return given().log().all()
                .when().get(url, pathParams)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> post(String url, Object request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> put(String url, Long pathParams, Object request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(url, pathParams)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> delete(String url, Long pathParams) {
        return given().log().all()
                .when().delete(url, pathParams)
                .then().log().all()
                .extract();
    }
}
