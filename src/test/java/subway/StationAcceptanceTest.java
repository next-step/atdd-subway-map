package subway;

import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    public static final String START_SUBWAY = "강남역";
    public static final String END_SUBWAY = "교대역";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역_생성() {
        // when
        ExtractableResponse<Response> response = createStationByName(START_SUBWAY);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getStationNames()
                .jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(START_SUBWAY);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void 지하철역_조회() {
        //given
        createStationByName(START_SUBWAY);
        createStationByName(END_SUBWAY);

        //when
        ExtractableResponse<Response> response = getStationNames();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAll(List.of(START_SUBWAY, END_SUBWAY));
        assertThat(stationNames).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void 지하철역_제거() {
        //given
        long startId = createStationByName(START_SUBWAY).jsonPath().getLong("id");
        createStationByName(END_SUBWAY);

        //when
        ExtractableResponse<Response> response = deleteStationById(startId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<String> stationNames = getStationNames().jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain(START_SUBWAY);

    }

    private static ExtractableResponse<Response> createStationByName(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
    private static ExtractableResponse<Response> getStationNames() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }
    private static ExtractableResponse<Response> deleteStationById(Long id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/{id}", id)
                .then().log().all()
                .extract();
    }
}