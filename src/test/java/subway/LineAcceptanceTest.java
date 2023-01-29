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

import java.util.List;

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
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // Given
        LineCreateRequest lineFixture = new LineCreateRequest("신분당선", "bg-red-600", 1L, 2L, 10L);

        // when
        ExtractableResponse<Response> createResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(lineFixture)
            .when().post("/lines")
            .then().log().all()
            .extract();

        String actualLineId = createResponse.jsonPath().getString("id");

        // Then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(actualLineId).isEqualTo("1");
        assertThat(createResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(createResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
        List<StationResponse> stations = createResponse.jsonPath().getList("stations", StationResponse.class);
        assertThat(stations).containsExactlyInAnyOrder(new StationResponse(1L, "지하철역"), new StationResponse(2L, "새로운지하철역"));

        ExtractableResponse<Response> listLineResponse = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .pathParam("lineId", actualLineId)
            .when().get("/lines/{lineId}")
            .then().log().all()
            .extract();

        assertThat(listLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(listLineResponse.jsonPath().getString("id")).isEqualTo("1");
        assertThat(listLineResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(listLineResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
        List<StationResponse> lineStations = createResponse.jsonPath().getList("stations", StationResponse.class);
        assertThat(lineStations).containsExactlyInAnyOrder(new StationResponse(1L, "지하철역"), new StationResponse(2L, "새로운지하철역"));
    }

}
