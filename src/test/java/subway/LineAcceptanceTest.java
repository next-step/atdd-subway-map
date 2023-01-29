package subway;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.web.request.LineCreateRequest;
import subway.web.response.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    private static RequestSpecification REQUEST_SPEC;

    @BeforeEach
    public void setUp() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(ContentType.JSON);
        REQUEST_SPEC = reqBuilder.build();
    }

    /**
     * Given 지하철역을 생성하고
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성하고 생성한 노선을 조회 할 수 있다")
    @Test
    void createStation() {
        // Given
        StationResponse 지하철역 = createStation("지하철역").as(StationResponse.class);
        StationResponse 새로운지하철역 = createStation("새로운지하철역").as(StationResponse.class);

        // When
        LineCreateRequest givenLine = new LineCreateRequest("신분당선", "bg-red-600", 1L, 2L, 8L);
        ExtractableResponse<Response> createLineResponse = createLine(givenLine, 1L, 지하철역, 새로운지하철역);

        String actualLineId = createLineResponse.jsonPath().getString("id");
        // Then
        ExtractableResponse<Response> listLineResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", actualLineId)
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract();

        assertThat(listLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(listLineResponse.jsonPath().getString("id")).isEqualTo("1");
        assertThat(listLineResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(listLineResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
        List<StationResponse> lineStations = createLineResponse.jsonPath().getList("stations", StationResponse.class);
        assertThat(lineStations).containsExactlyInAnyOrder(new StationResponse(1L, "지하철역"), new StationResponse(2L, "새로운지하철역"));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("생성한 노선 목록을 조회 할 수 있다")
    @Test
    void loadStations() {
        // Given
        StationResponse 지하철역 = createStation("지하철역").as(StationResponse.class);
        StationResponse 새로운지하철역 = createStation("새로운지하철역").as(StationResponse.class);
        StationResponse 또다른지하철역 = createStation("또다른지하철역").as(StationResponse.class);

        LineCreateRequest given_신분당선 = new LineCreateRequest("신분당선", "bg-red-600", 지하철역.getId(), 새로운지하철역.getId(), 10L);
        long given_신분당선_id = 1L;
        createLine(given_신분당선, given_신분당선_id, 지하철역, 새로운지하철역);

        LineCreateRequest given_분당선 = new LineCreateRequest("분당선", "bg-green-600", 지하철역.getId(), 또다른지하철역.getId(), 10L);
        long given_분당선_id = 2L;
        createLine(given_분당선, given_분당선_id, 지하철역, 또다른지하철역);

        // when
        ExtractableResponse<Response> actualResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        // Then
        assertThat(actualResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualResponse.jsonPath().getLong("[0].id")).isEqualTo(given_신분당선_id);
        assertThat(actualResponse.jsonPath().getString("[0].name")).isEqualTo("신분당선");
        assertThat(actualResponse.jsonPath().getString("[0].color")).isEqualTo("bg-red-600");
        List<StationResponse> _0_indexStations = actualResponse.jsonPath().getList("[0].stations", StationResponse.class);
        assertThat(_0_indexStations).containsExactlyInAnyOrder(지하철역, 새로운지하철역);

        assertThat(actualResponse.jsonPath().getLong("[1].id")).isEqualTo(given_분당선_id);
        assertThat(actualResponse.jsonPath().getString("[1].name")).isEqualTo("분당선");
        assertThat(actualResponse.jsonPath().getString("[1].color")).isEqualTo("bg-green-600");
        List<StationResponse> _1_indexStations = actualResponse.jsonPath().getList("[1].stations", StationResponse.class);
        assertThat(_1_indexStations).containsExactlyInAnyOrder(지하철역, 또다른지하철역);
    }

    private static ExtractableResponse<Response> createLine(LineCreateRequest lineCreateRequest, Long lineId, StationResponse upStation, StationResponse downStation) {

        // when
        ExtractableResponse<Response> createResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(lineCreateRequest)
            .when().post("/lines")
            .then().log().all()
            .extract();

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.jsonPath().getLong("id")).isEqualTo(lineId);
        assertThat(createResponse.jsonPath().getString("name")).isEqualTo(lineCreateRequest.getName());
        assertThat(createResponse.jsonPath().getString("color")).isEqualTo(lineCreateRequest.getColor());
        List<StationResponse> stations = createResponse.jsonPath().getList("stations", StationResponse.class);
        assertThat(stations).containsExactlyInAnyOrder(upStation, downStation);

        return createResponse;
    }

    private static ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(params)
            .when().post("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

}
