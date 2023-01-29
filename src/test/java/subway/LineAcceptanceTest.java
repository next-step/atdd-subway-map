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
