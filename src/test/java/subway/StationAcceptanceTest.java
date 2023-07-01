package subway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = 지하철역_목록조회_이름반환();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    private List<String> 지하철역_목록조회_이름반환() {
        return RestAssured.given().log().all()
                          .when().get("/stations")
                          .then().log().all()
                          .extract().jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                   .body(params)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when().post("/stations")
                   .then().log().all()
                   .extract();
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void getStationList() {
        지하철역_생성("삼전역");
        지하철역_생성("종합운동장역");

        // when
        // 지하철역 목록 조회
        // then
        // 2개의 지하철 역을 응답으로 받는다
        assertThat(지하철역_목록조회_이름반환()).containsAnyOf("삼전역", "종합운동장역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성

    @DisplayName("생성된 지하철역을 제거한다")
    @Test
    void deleteStations() {

    }

}