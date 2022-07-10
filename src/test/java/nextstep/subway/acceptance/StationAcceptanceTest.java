package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.AcceptanceTest;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.common.RestAssuredTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @Test
    void 지하철역을_생성한다() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @Test
    void 지하철역을_조회한다() {
        // given
        역을_만들다("잠실역");
        역을_만들다("한성백제역");

        // when
        var 지하철역_목록 = 지하철역_목록을_조회한다();

        // then
        var 지하철역_이름_목록 = 지하철역_목록.jsonPath().getList("name");
        assertThat(지하철역_이름_목록).containsExactly("잠실역", "한성백제역");

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @Test
    void 지하철역을_제거한다() {
        // given
        var 잠실역 = 역을_만들다("잠실역").as(StationResponse.class);

        // when
        역을_삭제한다(잠실역.getId());

        // then
        var 지하철역_목록 = 지하철역_목록을_조회한다();
        var 지하철역_이름_목록 = 지하철역_목록.jsonPath().getList("name", String.class);
        assertThat(지하철역_이름_목록).isEmpty();
    }


    public static ExtractableResponse<Response> 역을_만들다(String name) {
        var stationRequest = new StationRequest(name);
        var response = postRequestWithRequestBody("/stations", stationRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private ExtractableResponse<Response> 지하철역_목록을_조회한다() {
        var response = getRequest("/stations");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private void 역을_삭제한다(Long id) {
        var response = deleteRequestWithParameter("/stations/{id}", id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
