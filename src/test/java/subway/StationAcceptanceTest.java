package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    final String END_POINT = "/stations";
    final String KEY_NAME = "name";
    final String KEY_ID = "id";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final String STATION = "강남역";

        // when
        ExtractableResponse<Response> response = 지하철역_생성(STATION);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get(END_POINT)
                        .then().log().all()
                        .extract().jsonPath().getList(KEY_NAME, String.class);
        지하철역_이름_검증(STATION, stationNames);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 목록 조회")
    @Test
    void viewStationList(){
        //given
        final String STATION_NAME_1 = "강남역";
        final String STATION_NAME_2 = "숭실대입구역";
        지하철역_생성(STATION_NAME_1);
        지하철역_생성(STATION_NAME_2);

        //when
        ExtractableResponse<Response> getResponse = 지하철역_목록_조회();

        //then
        List<String> stationsNames = getResponse.jsonPath().getList(KEY_NAME, String.class);
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        지하철역_이름_검증(STATION_NAME_1, stationsNames);
        지하철역_이름_검증(STATION_NAME_2, stationsNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 제거")
    @Test
    void removeStation(){
        //given
        final String STATION = "강남역";
        ExtractableResponse<Response> createResponse = 지하철역_생성(STATION);
        long id = createResponse.jsonPath().getLong(KEY_ID);

        //when
        지하철역_삭제(id);

        //then
        ExtractableResponse<Response> getResponse = 지하철역_목록_조회();
        List<Long> stationsIds = getResponse.jsonPath().getList(KEY_ID, Long.class);
        assertThat(stationsIds).doesNotContain(id);
    }

    private void 지하철역_이름_검증(String station1, List<String> stationsNames) {
        assertThat(stationsNames).containsAnyOf(station1);
    }

    private void 지하철역_삭제(Long id){
        RestAssured.given().log().all()
            .when().delete(END_POINT + "/" + id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured.given().log().all()
                .when().get(END_POINT)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_생성(String value) {
        Map<String, String> params = new HashMap<>();
        params.put(KEY_NAME, value);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(END_POINT)
                .then().log().all()
                .extract();
    }
}