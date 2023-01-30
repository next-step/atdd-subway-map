package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = createLineResponse(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 5));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    private ExtractableResponse<Response> createLineResponse(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void showLines() {
        // given
        ExtractableResponse<Response> shinBunDang = createLineResponse(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 5));
        ExtractableResponse<Response> bunDang = createLineResponse(new LineRequest("분당선", "bg-red-700", 1L, 2L, 5));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<String> lineNames = response.jsonPath().getList("name", String.class);
        String shinBunDangLine = shinBunDang.jsonPath().getString("name");
        String bunDangLine = bunDang.jsonPath().getString("name");
        assertThat(lineNames).containsAnyOf(shinBunDangLine, bunDangLine);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void showLine() {
        // given
        ExtractableResponse<Response> shinBunDang = createLineResponse(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 5));

        // when
        long id = shinBunDang.jsonPath().getLong("id");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        String lineName = response.jsonPath().getString("name");
        String shinBunDangName = shinBunDang.jsonPath().getString("name");
        assertThat(lineName).isEqualTo(shinBunDangName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine() {
        // given
        ExtractableResponse<Response> shinBunDang = createLineResponse(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 5));

        // when
        long id = shinBunDang.jsonPath().getLong("id");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(new LineRequest("당당선", "bg-red-500"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        String lineName = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().jsonPath().getString("name");
        assertThat(lineName).isEqualTo("당당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> shinBunDang = createLineResponse(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 5));

        // when
        long id = shinBunDang.jsonPath().getLong("id");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<String> lineNames = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        // then
        String shinBunDangLine = shinBunDang.jsonPath().getString("name");
        assertThat(lineNames).doesNotContain(shinBunDangLine);
    }

}
