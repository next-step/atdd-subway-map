package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.api.LineApiCall;
import nextstep.subway.api.StationApiCall;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineUpdateDto;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@Sql({"classpath:subway_truncate.sql"})
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

        final String 강남역 = "강남역";
        final String 광교역 = "광교역";

        final String 신분당선 = "신분당선";

        ExtractableResponse<Response> stationCreationResponse1 = StationApiCall.createStation(new StationRequest(강남역));
        ExtractableResponse<Response> stationCreationResponse2 = StationApiCall.createStation(new StationRequest(광교역));

        Long 강남역_아이디 = getId(stationCreationResponse1);
        Long 광교역_아이디 = getId(stationCreationResponse2);

        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(신분당선, "bg-red-600", 강남역_아이디, 광교역_아이디, 10));
        assertThat(lineCreationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> getResponse = LineApiCall.getLines();

        String lineName = getResponse.jsonPath().getString("[0].name");
        String lineColor = getResponse.jsonPath().getString("[0].color");

        assertThat(lineName).isEqualTo(신분당선);
        assertThat(lineColor).isEqualTo("bg-red-600");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {

        final String 강남역 = "강남역";
        final String 판교역 = "판교역";
        final String 부평구청역 = "부평구청역";
        final String 장암역 = "장암역";

        final String 신분당선 = "신분당선";
        final String 칠호선 = "7호선";

        ExtractableResponse<Response> stationCreationResponse1 = StationApiCall.createStation(new StationRequest(강남역));
        ExtractableResponse<Response> stationCreationResponse2 = StationApiCall.createStation(new StationRequest(판교역));
        ExtractableResponse<Response> stationCreationResponse3 = StationApiCall.createStation(new StationRequest(부평구청역));
        ExtractableResponse<Response> stationCreationResponse4 = StationApiCall.createStation(new StationRequest(장암역));

        Long 강남역_아이디 = getId(stationCreationResponse1);
        Long 판교역_아이디 = getId(stationCreationResponse2);
        Long 부평구청역_아이디 = getId(stationCreationResponse3);
        Long 장암역_아이디 = getId(stationCreationResponse4);

        LineApiCall.createLine(new LineRequest(신분당선, "bg-red-600", 강남역_아이디, 판교역_아이디, 10));
        LineApiCall.createLine(new LineRequest(칠호선, "bg-brown-600", 부평구청역_아이디, 장암역_아이디, 10));

        List<String> lineNames = LineApiCall.getLines().jsonPath().getList("name", String.class);
        assertThat(lineNames).contains("신분당선", "7호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLines() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "인천역");

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations");

        params.put("name", "소요산역");
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations");

        ExtractableResponse<Response> createResponse = createLine(new Line("1호선", "bg-blue-600", 1L, 2L, 10));
        Long lineId = createResponse.jsonPath().getLong("id");

        String name = "새로운노선";
        String color = "bg-deepblue-600";

        ExtractableResponse<Response> updateResponse = updateLine(lineId, new LineUpdateDto(name, color));
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> getResponse = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        List<String> lineNames = getResponse.jsonPath().getList("name", String.class);
        assertThat(lineNames).contains("새로운노선");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLines() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "수원역");

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations");

        params.put("name", "정자역");
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations");

        ExtractableResponse<Response> createResponse = createLine(new Line("분당선", "bg-yellow-600", 1L, 2L, 10));

        Long lineId = createResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .when().delete("/lines/{id}", lineId)
                .then().log().all()
                .extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    // 응답객체 id 데이터 조회
    private Long getId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }


    // 지하철역 이름 조회
    private List<String> getStationNames() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    // 지하철 노선 업데이트
    private ExtractableResponse<Response> updateLine(Long id, LineUpdateDto updateDto) {
        return RestAssured.given().log().all()
                .body(updateDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }




}
