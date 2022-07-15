package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest{

    private long 삼성역;
    private long 역삼역;
    private long 강남역;
    private long 판교역;
    private long 신분당선;

    @BeforeEach
    public void setUp() {
        super.setUp();
        삼성역 = 지하철역_추가("삼성역").jsonPath().getLong("id");
        역삼역 = 지하철역_추가("역삼역").jsonPath().getLong("id");
        강남역 = 지하철역_추가("강남역").jsonPath().getLong("id");
        판교역 = 지하철역_추가("판교역").jsonPath().getLong("id");

        신분당선 = 지하철_노선_추가("신분당선","bg-red-600", 삼성역, 역삼역,10).jsonPath().getLong("id");
    }

    /**
     * When 지하철 구간을 등록하면
     * Then 지하철 구간 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void 지하철_구간_등록() {
        // when
        Map<String, String> params1 = new HashMap<>();
        params1.put("lineId", String.valueOf(신분당선));
        params1.put("upStationId", String.valueOf(역삼역));
        params1.put("downStationId", String.valueOf(강남역));
        params1.put("distance", String.valueOf(10));

        ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("lines/"+신분당선+"/sections")
                .then().log().all()
                .extract();

        assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        Map<String, String> params2 = new HashMap<>();
        params2.put("lineId", String.valueOf(신분당선));
        params2.put("upStationId", String.valueOf(강남역));
        params2.put("downStationId", String.valueOf(판교역));
        params2.put("distance", String.valueOf(7));

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("lines/"+신분당선+"/sections")
                .then().log().all()
                .extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        List<Long> 구간_목록 = RestAssured.given().log().all()
                .when().get("/lines/" + 신분당선)
                .then().log().all()
                .extract().jsonPath().getList("stations.id", Long.class);

        assertThat(구간_목록).contains(역삼역, 강남역, 판교역);
    }

    /**
     * When 새로운 구간의 상행역이 기존 하행 종점역이 아닌 역을 등록하면
     * Then 예외가 발생한다
     */
    @DisplayName("새로운 구간의 상행역이 등록되어있는 하행 종점역이 아니면 등록할 수 없다.")
    @Test
    void 지하철_구간_등록_상행역_에러() {

    }

    /**
     * When 새로운 구간의 하행역이 이미 노선에 등록되어있는 역이면
     * Then 예외가 발생한다
     */
    @DisplayName("지하철 구간은 등록되어있는 역은 등록할 수 없다.")
    @Test
    void 지하철_구간_등록_하행역_에러() {

    }

//    지하철 노선에 구간을 제거하는 기능 구현
//    지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
//    지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
//    새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.

    /**
     * Given 지하철 구간을 등록하고
     * when 구간을 제거하면
     * Then 정상적으로 제거된다.
     */
    @DisplayName("지하철 구간을 제거한다.")
    @Test
    void 지하철_구간_제거() {

    }

    /**
     * Given 지하철 구간을 등록하고
     * when 마지막 구간이 아닌 구간을 제거하면
     * Then 예외가 발생한다.
     */
    @DisplayName("마지막 구간이 아닌 구간은 제거할 수 없다.")
    @Test
    void 지하철_구간_제거_마지막_구간_에러() {

    }

    /**
     * when 구간이 1개일 때 제거하면
     * Then 예외가 발생한다.
     */
    @DisplayName("구간이 1개일 때 제거할 수 없다.")
    @Test
    void 지하철_구간_제거_구간_개수_에러() {

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
}