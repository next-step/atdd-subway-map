package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    private static final String 강남_station = "강남역";
    private static final String 양재_station = "양재역";
    private static final String 교대_station = "교대역";
    private static final String 잠실_station = "잠실역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        ExtractableResponse<Response> createResponse = 지하철역_생성(강남_station);

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<String> stationNames = 지하철역_목록_조회();
        assertThat(stationNames).containsAnyOf(강남_station);
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findAllStations() {
        지하철역_생성(양재_station);
        지하철역_생성(교대_station);

        List<String> stationNames = 지하철역_목록_조회();

        assertThat(stationNames).hasSize(2);
        assertThat(stationNames).containsExactly(양재_station, 교대_station);
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        ExtractableResponse<Response> createResponse = 지하철역_생성(잠실_station);

        Long id = createResponse.jsonPath().getLong("id");
        ExtractableResponse<Response> deleteResponse = 지하철역_삭제(id);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<String> stationNames = 지하철역_목록_조회();
        assertThat(stationNames).doesNotContain(잠실_station);
    }


    private List<String> 지하철역_목록_조회() {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> 지하철역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_삭제(Long id){
        return given()
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }
}