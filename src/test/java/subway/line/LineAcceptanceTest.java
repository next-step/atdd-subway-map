package subway.line;

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
import subway.line.web.request.LineCreateRequest;
import subway.station.web.response.StationResponse;

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
        ExtractableResponse<Response> response = RestAssured.given().spec(REQUEST_SPEC).log().all()
            .body(lineFixture)
            .when().post("/lines")
            .then().log().all()
            .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        assertThat(stations).containsExactlyInAnyOrder(new StationResponse(0L, "지하철역"), new StationResponse(1L, "새로운지하철역"));

    }

}
