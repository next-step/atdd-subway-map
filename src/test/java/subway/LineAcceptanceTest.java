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
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
@Sql(value = {"/LineAcceptance.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineAcceptanceTest {

    @DisplayName("지하철 생성 요청을 보낸 후, 생성한 노선을 찾을 수 있음.")
    @Test
    void createLine() {
        //when 지하철 노선을 생성하면.
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        requestCreateLine(lineRequest);

        //지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있음.
        LineResponse lineResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("$", LineResponse.class).get(0);

        checkCreatedLineData(lineRequest, lineResponse);
    }

    @DisplayName("현재 DB에 있는 모든 지하철의 조회가 가능함.")
    @Test
    void showLines() {
//       Given 2개의 지하철 노선을 생성하고
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        requestCreateLine(lineRequest1);
        LineRequest lineRequest2 = new LineRequest("분당선", "bg-red-600", 1L, 3L, 15L);
        requestCreateLine(lineRequest2);

//      When 지하철 노선 목록을 조회하면
        List<LineResponse> lineResponses = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("$", LineResponse.class);

//      Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(lineResponses.size()).isEqualTo(2);
        checkCreatedLineData(lineRequest1, lineResponses.get(0));
        checkCreatedLineData(lineRequest2, lineResponses.get(1));
    }

    @DisplayName("DB에 노선 데이터가 있으면, 해당 지하철 노선은 조회가 가능함.")
    @Test
    void showLine() {
//       Given 지하철 노선을 생성하고
        LineRequest createReq = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        LineResponse createRes = parseCreateLineResponse(requestCreateLine(createReq));

//      When 지하철 노선 목록을 조회하면
        LineResponse lineResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line/" + createRes.getId())
                .then().log().all()
                .extract().jsonPath().getObject("$", LineResponse.class);

//      Then 지하철 노선 조회 시 노선을 조회할 수 있다.
        checkCreatedLineData(createReq, lineResponse);
    }

    @DisplayName("DB에 있는 노선 데이터의 이름과 색깔을 수정할 수 있음.")
    @Test
    void updateLine() {

        //Given 지하철 노선을 생성하고
        LineRequest createReq = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        LineResponse createRes = parseCreateLineResponse(requestCreateLine(createReq));

//      When 생성한 지하철 노선을 수정하면
        LineUpdateRequest updateRequest = new LineUpdateRequest("다른 분당선", "bg-blue-600");
        ExtractableResponse<Response> updateResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateRequest)
                .when().put("/line/" + createRes.getId())
                .then()
                .extract();
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

//      Then 해당 지하철 노선 정보는 수정된다
        LineResponse lineResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line/" + createRes.getId())
                .then()
                .extract().jsonPath().getObject("$", LineResponse.class);
        assertThat(lineResponse.getName()).isEqualTo(updateRequest.getName());
        assertThat(lineResponse.getColor()).isEqualTo(updateRequest.getColor());

    }

    @DisplayName("DB에 지하철 노선 데이터가 있으면, 삭제가 가능함.")
    @Test
    void deleteLine() {
//      Given 지하철 노선을 생성하고
        LineRequest createReq = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        LineResponse createdLineRes = parseCreateLineResponse(requestCreateLine(createReq));

//      When 생성한 지하철 노선을 삭제하면
        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/line/" + createdLineRes.getId())
                .then().log().all().extract();
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

//      Then 해당 지하철 노선 정보는 삭제된다
        List<LineResponse> deleteCheckRes = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("$", LineResponse.class);

        assertThat(deleteCheckRes.size()).isEqualTo(0);
    }

    private void checkCreatedLineData(LineRequest req, LineResponse res) {
        assertThat(req.getName()).isEqualTo(res.getName());
        assertThat(req.getColor()).isEqualTo(res.getColor());

        var stations = res.getStations();
        var upStation = stations.get(0);
        var downStation = stations.get(1);
        assertThat(req.getUpStationId()).isEqualTo(upStation.getId());
        assertThat(req.getDownStationId()).isEqualTo(downStation.getId());
    }

    private ExtractableResponse<Response> requestCreateLine(LineRequest req) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(req)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private LineResponse parseCreateLineResponse(ExtractableResponse<Response> res) {
        return res.jsonPath().getObject("$", LineResponse.class);
    }
}
