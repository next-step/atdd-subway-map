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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/insert_test_station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineAcceptanceTest {

    private static String GANGNAM = "강남역";
    private static String YEOKSAM = "역삼역";

    void assertThatLineReqAndLineRes(LineRequest req, LineResponse res) {
        assertThat(req.getName()).isEqualTo(res.getName());
        assertThat(req.getColor()).isEqualTo(res.getColor());
        assertThat(req.getUpStationId()).isEqualTo(res.getStations().get(0).getId());
        assertThat(req.getDownStationId()).isEqualTo(res.getStations().get(1).getId());
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        //when 지하철 노선을 생성하면.
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있음.
        LineResponse lineResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("$", LineResponse.class).get(0);

        assertThatLineReqAndLineRes(lineRequest, lineResponse);
    }

    @DisplayName("지하철 노선들 조회")
    @Test
    void showLines() {
//       Given 2개의 지하철 노선을 생성하고
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response1 = RestAssured.given()
                .body(lineRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().extract();

        assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        LineRequest lineRequest2 = new LineRequest("분당선", "bg-red-600", 1L, 3L, 15L);
        ExtractableResponse<Response> response2 = RestAssured.given()
                .body(lineRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());


//      When 지하철 노선 목록을 조회하면
        List<LineResponse> lineResponses = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("$", LineResponse.class);

//      Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
        assertThat(lineResponses.size()).isEqualTo(2);
        assertThatLineReqAndLineRes(lineRequest1 ,lineResponses.get(0));
        assertThatLineReqAndLineRes(lineRequest2 ,lineResponses.get(1));
    }

    @DisplayName("지하철 특정 노선 조회")
    @Test
    void showLine(){
//       Given 지하철 노선을 생성하고
        LineRequest createReq = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response = RestAssured.given()
                .body(createReq)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        LineResponse createRes = response.jsonPath().getObject("$", LineResponse.class);

//      When 지하철 노선 목록을 조회하면
        LineResponse lineResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line/" + createRes.getId())
                .then().log().all()
                .extract().jsonPath().getObject("$", LineResponse.class);

//      Then 지하철 노선 조회 시 노선을 조회할 수 있다.
        assertThatLineReqAndLineRes(createReq ,lineResponse);
    }

    @DisplayName("지하철 특정 노선 수정")
    @Test
    void updateLine(){

        //Given 지하철 노선을 생성하고
        LineRequest createReq = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response = RestAssured.given()
                .body(createReq)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        LineResponse createRes = response.jsonPath().getObject("$", LineResponse.class);

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

    @DisplayName("지하철 특정 노선 삭제")
    @Test
    void deleteLine(){
//        Given 지하철 노선을 생성하고
        LineRequest createReq = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response = RestAssured.given()
                .body(createReq)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        LineResponse createdLineRes = response.jsonPath().getObject("$", LineResponse.class);

//        When 생성한 지하철 노선을 삭제하면
        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/line/" + createdLineRes.getId())
                .then().log().all().extract();
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

//        Then 해당 지하철 노선 정보는 삭제된다
        List<LineResponse> deleteCheckRes = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract().jsonPath().getList("$", LineResponse.class);

        assertThat(deleteCheckRes.size()).isEqualTo(0);
    }

}
