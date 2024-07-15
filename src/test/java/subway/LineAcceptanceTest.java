package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationAcceptanceTest.createStation;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("노선 생성")
    @Test
    void createLine() {
        // given
        long gangnamStationId = createStation("강남역");
        long yeoksamStationId = createStation("역삼역");
        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600", gangnamStationId, yeoksamStationId, 10);

        // when
        createLine(lineRequest);

        // then
        List<String> lineNames = getLineNames();
        assertThat(lineNames).containsExactlyInAnyOrder("2호선");
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */

    @DisplayName("노선 목록 조회")
    @Test
    void getLines() {
        // given

        long gangnamStationId = createStation("강남역");
        long yeoksamStationId = createStation("역삼역");
        long yangjaeStationId = createStation("양재역");

        LineRequest lineNumberTwo = new LineRequest("2호선", "bg-green-600", gangnamStationId, yeoksamStationId, 10);
        LineRequest shinbundangLine = new LineRequest("신분당선", "bg-red-600", gangnamStationId, yangjaeStationId, 10);

        createLine(lineNumberTwo);
        createLine(shinbundangLine);

        // when
        List<String> lineNames = getLineNames();

        // then
        assertThat(lineNames).containsExactlyInAnyOrder("2호선", "신분당선");

    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */

    @DisplayName("노선 조회")
    @Test
    void getLine() {
        // given

        long gangnamStationId = createStation("강남역");
        long yeoksamStationId = createStation("역삼역");
        LineRequest lineNumberTwo = new LineRequest("2호선", "bg-green-600", gangnamStationId, yeoksamStationId, 10);

        ExtractableResponse<Response> line = createLine(lineNumberTwo);

        // when
        LineResponse lineResponse = getLine(line.as(LineResponse.class).getId());

        // then
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("bg-green-600");

    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */

    @DisplayName("노선 수정")
    @Test
    void updateLine() {
        // given

        long gangnamStationId = createStation("강남역");
        long yeoksamStationId = createStation("역삼역");
        LineRequest lineNumberTwo = new LineRequest("2호선", "bg-green-600", gangnamStationId, yeoksamStationId, 10);

        ExtractableResponse<Response> line = createLine(lineNumberTwo);
        LineResponse lineResponse = getLine(line.as(LineResponse.class).getId());

        // when
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("22호선", "bg-green-400");
        LineResponse updateLine = updateLine(lineResponse.getId(), lineUpdateRequest);

        // then
        assertThat(updateLine.getName()).isEqualTo("22호선");
        assertThat(updateLine.getColor()).isEqualTo("bg-green-400");

    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */

    @DisplayName("노선 삭제")
    @Test
    void deleteLine() {
        // given

        long gangnamStationId = createStation("강남역");
        long yeoksamStationId = createStation("역삼역");
        LineRequest lineNumberTwo = new LineRequest("2호선", "bg-green-600", gangnamStationId, yeoksamStationId, 10);

        ExtractableResponse<Response> line = createLine(lineNumberTwo);
        LineResponse lineResponse = getLine(line.as(LineResponse.class).getId());

        // when
        deleteLine(lineResponse.getId());

        // then
        List<String> lineNames = getLineNames();
        assertThat(lineNames).doesNotContain("2호선");
    }

    private void deleteLine(Long lineId) {
        RestAssured.given().log().all()
                .when().delete("/line/" + lineId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


    private LineResponse updateLine(Long lineId, LineUpdateRequest lineUpdateRequest) {
        return RestAssured.given().log().all()
                .body(lineUpdateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/line/" + lineId)
                .then().log().all()
                .extract().as(LineResponse.class);
    }

    private LineResponse getLine(Long lineId) {
        return RestAssured.given().log().all()
                .when().get("/line/" + lineId)
                .then().log().all()
                .extract().as(LineResponse.class);
    }

    private List<String> getLineNames() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> createLine(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract();

    }

}
