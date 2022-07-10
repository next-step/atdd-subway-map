package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest{
    @BeforeEach
    public void setUp() {
        super.setUp();
        지하철역_추가("삼성역");
        지하철역_추가("역삼역");
        지하철역_추가("강남역");
    }

    private ExtractableResponse<Response> 지하철역_추가(String name){
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성() {
        // when
        final ExtractableResponse<Response> response =
                지하철_노선_추가("신분당선","bg-red-600",1,2 ,10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> readResponse = 지하철_노선_목록();
        List<String> names = readResponse.jsonPath().getList("name", String.class);

        assertThat(names).containsAnyOf("신분당선");
    }

    private ExtractableResponse<Response> 지하철_노선_목록() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        지하철_노선_추가("신분당선","bg-red-600",1,2 ,10);
        지하철_노선_추가("분당선","bg-green-600",1,3,5);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> names = response.jsonPath().getList("name", String.class);
        assertThat(names).contains("신분당선", "분당선");
        assertThat(names).hasSize(2);

        List<String> stationNames = response.jsonPath().getList("stations.name", String.class);
        assertThat(stationNames).contains("[삼성역, 역삼역]", "[삼성역, 강남역]");
    }

    private ExtractableResponse<Response> 지하철_노선_추가(String name, String color, long upStationId, long downStationId, long distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선_조회() {
        // given
        final ExtractableResponse<Response> addResponse =
                지하철_노선_추가("신분당선","bg-red-600",1,2 ,10);
        final long id = addResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().get("/lines/" + id)
                        .then().log().all()
                        .extract();

        // then
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse.getName()).isEqualTo("신분당선");
        assertThat(lineResponse.getColor()).isEqualTo("bg-red-600");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void 지하철_노선_수정() {
        // given
        final ExtractableResponse<Response> addResponse =
                지하철_노선_추가("신분당선","bg-red-600",1,2 ,10);
        final long id = addResponse.jsonPath().getLong("id");

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "다른분당선");
        params.put("color", "bg-yellow-600");
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(params)
                        .when().put("/lines/" + id)
                        .then().log().all()
                        .extract();

        // then
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse.getName()).isEqualTo(params.get("name"));
        assertThat(lineResponse.getColor()).isEqualTo(params.get("color"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void 지하철_노선_삭제() {
        // given
        final ExtractableResponse<Response> addResponse =
                지하철_노선_추가("신분당선","bg-red-600",1,2 ,10);
        final long id = addResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when().delete("/lines/" + id)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}