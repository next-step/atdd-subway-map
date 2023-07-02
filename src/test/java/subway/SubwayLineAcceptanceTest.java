package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {

    private final String SUBWAY_LINE_API_URL = "/subway-lines";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        // when
        final String DEFAULT_SUBWAY_LINE_NAME = "신분당선";
        final String DEFAULT_SUBWAY_LINE_COLOR = "bg-red-600";
        final Integer DEFAULT_UP_STATION_ID = 1;
        final Integer DEFAULT_DOWN_STATION_ID = 2;
        final Integer DEFAULT_DISTANCE = 10;

        Map<String, Object> parameters = Map.of("name", DEFAULT_SUBWAY_LINE_NAME, "color", DEFAULT_SUBWAY_LINE_COLOR
                        , "upStationId", DEFAULT_UP_STATION_ID, "downStationId", DEFAULT_DOWN_STATION_ID,
                        "distance", DEFAULT_DISTANCE);

        ExtractableResponse<Response> createSubwayLineApiResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(parameters)
                .when().log().all()
                    .post(SUBWAY_LINE_API_URL)
                .then().log().all()
                    .extract();

        // then
        String actualSubwayLineName = RestAssured
                .given().log().all()
                .when().log().all()
                    .get(SUBWAY_LINE_API_URL)
                .then().log().all()
                .extract()
                    .jsonPath().getString("name");

        Assertions.assertThat(actualSubwayLineName).isEqualTo(DEFAULT_SUBWAY_LINE_NAME);
    }

    ExtractableResponse<Response> 지하철_노션_요청(Map<String, Object> parameters) {
        ExtractableResponse<Response> createSubwayLineApiResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(parameters)
                .when().log().all()
                    .post(SUBWAY_LINE_API_URL)
                .then().log().all()
                    .extract();

        return createSubwayLineApiResponse;
    }

    /**
     * Given 2개의 지하철 노션을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showSubwayLine() {
        // given
        List<Map<String, Object>> parametersList = new ArrayList<>();
        Map<String, Object> subwayLine1Parameters = Map.of("name", "1호선", "color", "bg-blue-600"
                , "upStationId", 1, "downStationId", 5,
                "distance", 4);
        Map<String, Object> subwayLine2Parameters = Map.of("name", "2호선", "color", "bg-blue-600"
                , "upStationId", 6, "downStationId", 10,
                "distance", 4);
        parametersList.add(subwayLine1Parameters);
        parametersList.add(subwayLine2Parameters);

        for (Map<String, Object> parameters : parametersList) {
            지하철_노션_요청(parameters);
        }

        // when
        List<String> subwayLineNames = RestAssured
                .given().log().all()
                .when().log().all()
                    .get(SUBWAY_LINE_API_URL)
                .then().log().all()
                .extract()
                    .jsonPath().getList("name", String.class);

        // then
        Assertions.assertThat(subwayLineNames).contains("1호선", "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답 받을 수 있다.
     */
    @DisplayName("지하철 노선 정보를 조회한다.")
    @Test
    void showSubwayLineInfo() {
        // given
        Map<String, Object> subwayLineParameters = Map.of("name", "1호선", "color", "bg-blue-600"
                , "upStationId", 1, "downStationId", 5,
                "distance", 4);
        지하철_노션_요청(subwayLineParameters);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().log().all()
                    .get(SUBWAY_LINE_API_URL)
                .then().log().all()
                .extract();

        String actualName = response.jsonPath().getString("name");
        String actualColor = response.jsonPath().getString("color");
        Integer actualUpStationId = response.jsonPath().getInt(".stations[0].id");
        Integer actualDownStationId = response.jsonPath().getInt(".stations[1].id");

        // then
        Assertions.assertThat(actualName).isEqualTo("1호선");
        Assertions.assertThat(actualColor).isEqualTo("bg-blue-600");
        Assertions.assertThat(actualUpStationId).isEqualTo(1);
        Assertions.assertThat(actualDownStationId).isEqualTo(5);
    }

    /**
     * Given 지하철 노션을 생성하고
     * When 생성한 지하철 노션 정보를 수정하면
     * Then 해당 지하철 노션 정보는 수정된다.
     */
    @DisplayName("지하철 노션 정보를 수정한다.")
    @Test
    void updateSubwayLineInfo() {
        // given
        Map<String, Object> subwayLineParameters = Map.of("name", "1호선", "color", "bg-blue-600"
                , "upStationId", 1, "downStationId", 5,
                "distance", 4);
        ExtractableResponse<Response> createSubwayLineApiResponse = 지하철_노션_요청(subwayLineParameters);
        long responseId = createSubwayLineApiResponse.jsonPath().getLong("id");

        // when
        Map<String, Object> updateLineParameters = Map.of("name", "1호선", "color", "bg-green-600"
                , "upStationId", 1, "downStationId", 5,
                "distance", 4);

        ExtractableResponse<Response> updateSubwayLineApiResponse = RestAssured
                .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(updateLineParameters)
                .when().log().all()
                    .put(SUBWAY_LINE_API_URL + "/" + responseId)
                .then().log().all()
                .extract();

        // then
        assertThat(updateSubwayLineApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노션을 생성하고
     * When 생성한 지하철 노션을 삭제하면
     * Then 해당 지하철 노션 장보는 삭제된다.
     */
    @DisplayName("지하철 노션을 삭제한다.")
    @Test
    void deleteSubwayLine() {
        // given
        Map<String, Object> subwayLineParameters = Map.of("name", "1호선", "color", "bg-blue-600"
                , "upStationId", 1, "downStationId", 5,
                "distance", 4);
        ExtractableResponse<Response> createSubwayLineApiResponse = 지하철_노션_요청(subwayLineParameters);
        long responseId = createSubwayLineApiResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteSubwayLineApiResponse = RestAssured
                .given().log().all()
                .when().log().all()
                    .delete(SUBWAY_LINE_API_URL + "/" + responseId)
                .then().log().all()
                .extract();

        // then
        assertThat(deleteSubwayLineApiResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}