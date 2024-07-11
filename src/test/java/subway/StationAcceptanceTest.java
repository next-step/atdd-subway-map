package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.test.AcceptanceTestBase;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTestBase {
    public static final String GANGNAM_STATION = "강남역";
    public static final String SEOUL_STATION = "서울역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다")
    @Test
    void createStation() {
        // when
        var createResponse = requestCreateStation(GANGNAM_STATION);
        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        var getAllResponse = requestGetAllStations();
        assertThat(extractNames(getAllResponse)).containsAnyOf(GANGNAM_STATION);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void getAllStation() {
        //given
        requestCreateStation(GANGNAM_STATION);
        requestCreateStation(SEOUL_STATION);

        //when
        var getAllResponse = requestGetAllStations();

        //then
        assertThat(getAllResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extractIds(getAllResponse).size()).isEqualTo(2);

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다")
    @Test
    void deleteStation() {
        //given
        var createdResponse = requestCreateStation(SEOUL_STATION);
        var createdId = createdResponse.jsonPath().getLong("id");

        //when
        requestDeleteStation(createdId);

        //then
        var getAllResponse = requestGetAllStations();
        assertThat(extractIds(getAllResponse)).doesNotContain(createdId);
    }

    private ExtractableResponse<Response> requestCreateStation(String name) {
        var body = Map.of("name", name);
        var contentType = MediaType.APPLICATION_JSON_VALUE;
        var path = "/stations";

        return RestAssured.given().log().all()
                .body(body)
                .contentType(contentType)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestGetAllStations() {
        return RestAssured
                .when().get("/stations")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> requestDeleteStation(Long id) {
        return RestAssured.given()
                .pathParam("id", id)
                .when()
                .delete("/stations/{id}")
                .then()
                .extract();
    }

    private List<String> extractNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

    private List<Long> extractIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("id", Long.class);
    }
}
