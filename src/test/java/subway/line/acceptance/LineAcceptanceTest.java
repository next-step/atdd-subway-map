package subway.line.acceptance;

import core.RestAssuredHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.line.service.dto.LineResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private RestAssuredHelper lineRestAssured;
    private RestAssuredHelper stationRestAssured;

    @BeforeEach
    void setUp() {
        lineRestAssured = new RestAssuredHelper("/lines");
        stationRestAssured = new RestAssuredHelper("/stations");
    }

    /**
     * When 노선을 생성하면
     * Then 노선이 생성된다
     * Then 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLineTest() {
        // when
        final Long upStationId = createStation("지하철역");
        final Long downStationId = createStation("새로운지하철역");

        final Map<String, Object> lineCreateRequestData = Map.of(
                "name", "신분당선"
                , "color", "bg-red-600"
                , "upStationId", upStationId
                , "downStationId", downStationId
                , "distance", 10
        );
        final ExtractableResponse<Response> response = lineRestAssured.post(lineCreateRequestData);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            final LineResponse lineResponse = response.as(LineResponse.class);
            softly.assertThat(lineResponse.getName()).isEqualTo("신분당선");
            softly.assertThat(lineResponse.getColor()).isEqualTo("bg-red-600");
            softly.assertThat(lineResponse.getStations())
                    .extracting("id").containsExactly(1L, 2L);
        });

        // then
        final ExtractableResponse<Response> linesResponse = lineRestAssured.get();
        final List<String> lineNames = linesResponse.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("신분당선");
    }

    private Long createStation(final String name) {
        return stationRestAssured.post(Map.of("name", name)).jsonPath().getLong("id");
    }

}
