package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.StationApiClient;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private Long upStationId;
    private Long downStationId;

    @BeforeEach
    void initStations() {
        ExtractableResponse<Response> upStationResponse = StationApiClient.requestCreateStation("장암");
        this.upStationId = upStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> downStationResponse = StationApiClient.requestCreateStation("도봉산");
        this.downStationId = downStationResponse.jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        String nameForRequest = "7호선";

        LineRequest req = LineRequest.builder()
                .name(nameForRequest)
                .color("#747F00")
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(12)
                .build();

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(req)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        String nameFromResponse = response.jsonPath().getString("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(nameFromResponse).isEqualTo(nameForRequest);
    }
}
