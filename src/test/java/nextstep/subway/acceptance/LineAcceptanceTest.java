package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {



    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        // 요청에 대한 데이터
        Map<String, String> request = new HashMap<>();
        request.put("color", "color_1");
        request.put("name", "name_1");

        // given, when, then
        ExtractableResponse<Response> extract = RestAssured
                .given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .when().post("/lines")
                .then().log().all()
                .extract();

        // 상태 코드
        assertThat(extract.response().statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(extract.header("Location")).isNotBlank();
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

        // 지하철 노선 1을 생성
        Map<String, String> request1 = new HashMap<>();
        request1.put("color", "color_1");
        request1.put("name", "name_1");

        // 지하철 노선 2를 생성
        Map<String, String> request2 = new HashMap<>();
        request2.put("color", "color_2");
        request2.put("name", "name_2");

        // 요청을 하고 생성을 했을 때
        ExtractableResponse<Response> request1Response = RestAssured
                .given().body(request1).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .when().post("/lines")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> request2Response = RestAssured
                .given().body(request2).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .when().post("/lines")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> resultResponse = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();

        // 조회 포함 확인
        assertThat(resultResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> resultResponseData = resultResponse.jsonPath().getList("color");
        assertThat(resultResponseData).contains("color_1", "color_2");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
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
