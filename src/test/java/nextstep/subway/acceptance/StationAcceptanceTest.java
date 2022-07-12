package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends BaseAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        지하철역_등록_요청("강남역");

        // then
        ExtractableResponse<Response> stationResponse = 지하철역_목록_조회_요청();
        List<String> 지하철역_ID_목록 = stationResponse.jsonPath().getList("name", String.class);
        assertThat(지하철역_ID_목록).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    @DisplayName("모든 지하철 역을 조회한다.")
    void getStations() {
        // given
        지하철역_등록_요청("강남역");
        지하철역_등록_요청("잠실역");

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        //then
        List<String> 지하철역_이름_목록 = response.jsonPath().getList("name", String.class);
        assertThat(지하철역_이름_목록).containsOnly("강남역", "잠실역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    @DisplayName("지하철 역을 제거한다.")
    void deleteStation() {
        // given
        ExtractableResponse<Response> response = 지하철역_등록_요청("강남역");
        long 강남역_ID = response.jsonPath().getLong("id");

        // when
        지하철역_제거_요청(강남역_ID);

        // then
        ExtractableResponse<Response> stationResponse = 지하철역_목록_조회_요청();
        List<Long> 지하철역_ID_목록 = stationResponse.jsonPath().getList("id", Long.class);
        assertThat(지하철역_ID_목록).doesNotContain(강남역_ID);
    }

    public static ExtractableResponse<Response> 지하철역_등록_요청(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(long stationId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/stations/{id}", stationId)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        return response;
    }
}