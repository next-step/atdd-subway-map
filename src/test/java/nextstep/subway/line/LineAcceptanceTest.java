package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@AcceptanceTest
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    private Long upStationId;
    private Long downStationId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        upStationId = StationAcceptanceTest.createStationRequest("강남역").jsonPath().getObject("id", Long.class);
        downStationId = StationAcceptanceTest.createStationRequest("양재역").jsonPath().getObject("id", Long.class);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        final String lineName = "신분당선";
        final Map<String, Object> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);

        final ExtractableResponse<Response> createLineResponse = RestAssured
                .given().log().all()
                .body(params)
                .when().post("/lines")
                .then().log().all().extract();

        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        final List<Long> idList = createLineResponse.jsonPath().getList("id", Long.class);
        assertThat(idList).contains(upStationId, downStationId);
    }

}
