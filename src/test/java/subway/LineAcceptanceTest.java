package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.LineRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.StationResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private static Long GANGNAM_STATION_ID;
    private static Long SEOLLEUNG_STATION_ID;
    private static Long YANGJAE_STATION_ID;

    // TODO: 중복 제거 필요
    @BeforeEach
    void setFixture() {
        GANGNAM_STATION_ID = RestAssured.given()
                .body(Map.of("name", "강남역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then()
                .extract().as(StationResponse.class).getId();

        SEOLLEUNG_STATION_ID = RestAssured.given()
                .body(Map.of("name", "선릉역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then()
                .extract().as(StationResponse.class).getId();

        YANGJAE_STATION_ID = RestAssured.given()
                .body(Map.of("name", "양재역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then()
                .extract().as(StationResponse.class).getId();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        LineRequest request = new LineRequest(
                "신분당선",
                "bg-red-600",
                GANGNAM_STATION_ID,
                SEOLLEUNG_STATION_ID,
                10L
        );

        // when
        ExtractableResponse<Response> createResponse = createLine(request);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        LineResponse lineResponse = createResponse.as(LineResponse.class);

        ExtractableResponse<Response> findResponse = findLines();
        List<Long> lineIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(lineIds).containsAnyOf(lineResponse.getId());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void selectLines() {
        // given
        LineRequest shinbundangRequest = new LineRequest(
                "신분당선",
                "bg-red-600",
                GANGNAM_STATION_ID,
                SEOLLEUNG_STATION_ID,
                10L
        );
        ExtractableResponse<Response> shinbundangCreateResponse = createLine(shinbundangRequest);
        assertThat(shinbundangCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        LineRequest bundangRequest = new LineRequest(
                "분당선",
                "bg-green-600",
                GANGNAM_STATION_ID,
                YANGJAE_STATION_ID,
                10L
        );
        ExtractableResponse<Response> bundangCreateResponse = createLine(bundangRequest);
        assertThat(bundangCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> findResponse = findLines();
        List<String> linesNames = findResponse.jsonPath().getList("name", String.class);

        // then
        assertThat(linesNames).hasSize(2)
                .containsExactly("신분당선", "분당선");
    }

    private ExtractableResponse<Response> createLine(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

}
