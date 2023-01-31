package subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        // given
        final String stationName = "강남역";

        // when
        var response = 지하철역_등록_요청(stationName);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(지하철역_조회_요청().jsonPath().getList("name")).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStationList() {
        // given
        final String firstStationName = "강남역";
        final String secondStationName = "양재역";

        지하철역_등록_성공_응답(firstStationName);
        지하철역_등록_성공_응답(secondStationName);

        // when
        var response = 지하철역_조회_요청();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        assertThat(response.jsonPath().getList("name")).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        final String stationName = "강남역";
        long stationId = 지하철역_등록_성공_응답(stationName).body().jsonPath().getLong("id");

        // when
        지하철역_삭제_요청(stationId);

        // then
        assertThat(지하철역_조회_요청().jsonPath().getList("name")).doesNotContain(stationName);
    }

    private ExtractableResponse<Response> 지하철역_등록_성공_응답(String stationName) {
        ExtractableResponse<Response> response = 지하철역_등록_요청(stationName);
        지하철역_등록_성공(response);
        return response;
    }

    private ExtractableResponse<Response> 지하철역_등록_요청(String stationName) {
        return RestAssured.given().log().all()
                .body(Map.of("name", stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract();
    }

    private void 지하철역_등록_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().statusCode(HttpStatus.OK.value())
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_삭제_요청(long stationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}", stationId)
                .then().statusCode(HttpStatus.NO_CONTENT.value())
                .log().all()
                .extract();
    }
}