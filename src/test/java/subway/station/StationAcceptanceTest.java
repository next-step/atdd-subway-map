package subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
        String 강남역 = "강남역";
        ExtractableResponse<Response> response = 지정된_이름의_지하철역을_생성하고_응답결과를_받아온다(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> responseOfShowStations = 지하철역_목록을_조회한다();
        List<String> stationNames = responseOfShowStations.jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf(강남역);
    }

    private ExtractableResponse<Response> 지정된_이름의_지하철역을_생성하고_응답결과를_받아온다(String stationName) {
        Map<String, String> params = Map.of("name", stationName);

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
    @DisplayName("지하철역 목록 조회")
    @Test
    void findAllStations() {
        //given
        String 가양역 = "가양역";
        지정된_이름의_지하철역을_생성한다(가양역);

        String 여의도역 = "여의도역";
        지정된_이름의_지하철역을_생성한다(여의도역);

        //when
        ExtractableResponse<Response> response = 지하철역_목록을_조회한다();

        //then
        List<String> stationNames = response.jsonPath().getList("name", String.class);

        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).contains(가양역, 여의도역);
    }

    private ExtractableResponse<Response> 지하철역_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/stations")  //TODO /stations 가 다른 path로 변경된다면?
                .then().log().all()
                .extract();
    }

    private void 지정된_이름의_지하철역을_생성한다(String stationName) {
        Map<String, String> params = Map.of("name", stationName);

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all();
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStationById() {
        //given
        String 가양역 = "가양역";
        지정된_이름의_지하철역을_생성한다(가양역);

        ExtractableResponse<Response> responseOfShowStations = 지하철역_목록을_조회한다();
        List<Long> stationIds = responseOfShowStations.jsonPath().getList("id", Long.class);
        Long stationId = stationIds.get(0);

        //when
        ExtractableResponse<Response> responseOfDeleteStation = 지하철역을_삭제한다(stationId);

        //then
        assertThat(responseOfDeleteStation.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> responseOfShowStationsAfterDelete = 지하철역_목록을_조회한다();
        List<Long> stationIdsAfterDelete = responseOfShowStationsAfterDelete.jsonPath().getList("id", Long.class);

        assertThat(stationIdsAfterDelete).hasSize(0);
    }

    private ExtractableResponse<Response> 지하철역을_삭제한다(Long stationId) {
        return RestAssured.given().log().all()
                .pathParam("id", stationId)
                .when().delete("/stations/{id}")
                .then().log().all()
                .extract();
    }
}