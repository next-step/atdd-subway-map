package nextstep.subway.acceptance.acceptance;

import static nextstep.subway.acceptance.template.StationRequestTemplate.지하철역_목록을_조회한다;
import static nextstep.subway.acceptance.template.StationRequestTemplate.지하철역_삭제를_요청한다;
import static nextstep.subway.acceptance.template.StationRequestTemplate.지하철역을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> createdResponse = 지하철역을_생성한다("강남역");

        // then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> stationsResponse = 지하철역_목록을_조회한다();
        List<String> stationNames = stationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        지하철역을_생성한다("신도림역");
        지하철역을_생성한다("건대역");

        // when
        ExtractableResponse<Response> stationsResponse = 지하철역_목록을_조회한다();

        // then
        List<String> stationNames = stationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationNames).containsOnlyOnce("신도림역", "건대역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createdResponse = 지하철역을_생성한다("홍대입구역");
        long stationId = createdResponse.jsonPath().getLong("id");

        // when
        지하철역_삭제를_요청한다(stationId);

        // then
        ExtractableResponse<Response> stationsResponse = 지하철역_목록을_조회한다();
        List<String> stationName = stationsResponse.jsonPath().getList("name", String.class);

        assertThat(stationName).doesNotContain("홍대입구역");
    }
}