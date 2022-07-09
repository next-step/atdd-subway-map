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
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
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
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        역을_만들다("잠실역");
        역을_만들다("한성백제역");

        // when
        ExtractableResponse<Response> response = 지하철역_목록을_조회한다();

        // then
        List<String> stationNames = response.jsonPath().getList("name");
        assertThat(stationNames).containsExactly("잠실역", "한성백제역");

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse 잠실역 = 역을_만들다("잠실역").as(StationResponse.class);

        // when
        역을_삭제한다(잠실역.getId());

        // then
        ExtractableResponse<Response> response = 지하철역_목록을_조회한다();
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).isEmpty();
    }


    public static ExtractableResponse<Response> 역을_만들다(String name) {
        StationRequest stationRequest = new StationRequest(name);
        ExtractableResponse<Response> response = postRequestWithRequestBody("/stations", stationRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private ExtractableResponse<Response> 지하철역_목록을_조회한다() {
        ExtractableResponse<Response> response = getRequest("/stations");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    private void 역을_삭제한다(Long id) {
        ExtractableResponse<Response> response = deleteRequestWithParameter("/stations/{id}", id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
