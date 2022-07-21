package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.api.LineApi;
import nextstep.subway.api.StationApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
@Sql({"classpath:subway.init.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    private long 강남역;
    private long 신논현역;
    private long 정자역;
    private long 이매역;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        강남역 = StationApi.createStationApi("강남역").jsonPath().getLong("id");
        신논현역 = StationApi.createStationApi("신논현역").jsonPath().getLong("id");
        정자역 = StationApi.createStationApi("정자역").jsonPath().getLong("id");
        이매역 = StationApi.createStationApi("이매역").jsonPath().getLong("id");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createLine() {
        // When
        ExtractableResponse<Response> 신분당선 = LineApi.createLineApi("신분당선", "bg-red-600", 강남역, 신논현역, 10);

        // Then
        ExtractableResponse<Response> 모든_노선_조회_응답 = LineApi.getAllLinesApi();
        List<String> 지하철역_이름 = 모든_노선_조회_응답.jsonPath().getList("stations[0].name", String.class);

        assertAll(
                () -> assertThat(지하철역_이름).hasSize(2),
                () -> assertThat(지하철역_이름).containsAnyOf("강남역", "신논현역")
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines() {
        // Given
        ExtractableResponse<Response> 신분당선 = LineApi.createLineApi("신분당선", "bg-red-600", 강남역, 정자역, 10);
        ExtractableResponse<Response> 분당선 = LineApi.createLineApi("분당선", "bg-yellow-600", 정자역, 이매역, 10);

        // When
        ExtractableResponse<Response> 모든_노선_조회_응답 = LineApi.getAllLinesApi();
        List<String> 신분당선_역이름 = 모든_노선_조회_응답.jsonPath().get("stations[0].name");
        List<String> 분당선_역이름 = 모든_노선_조회_응답.jsonPath().get("stations[1].name");

        // Then
        assertAll(
                () -> assertThat(신분당선_역이름).hasSize(2),
                () -> assertThat(신분당선_역이름).containsAnyOf("강남역", "정자역"),
                () -> assertThat(분당선_역이름).hasSize(2),
                () -> assertThat(분당선_역이름).containsAnyOf("정자역", "이매역")
        );
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void getLineById() {
        // Given
        ExtractableResponse<Response> 신분당선 = LineApi.createLineApi("신분당선", "bg-red-600", 강남역, 정자역, 10);

        // When
        long lineId = 신분당선.jsonPath().getLong("id");
        ExtractableResponse<Response> 신분당선_조회_응답 = LineApi.getLineByIdApi(lineId);
        List<String> 지하철역_이름 = 신분당선_조회_응답.jsonPath().getList("stations.name", String.class);

        // Then
        assertAll(
                () -> assertThat(지하철역_이름).hasSize(2),
                () -> assertThat(지하철역_이름).containsAnyOf("강남역", "신논현역")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        // Given
        ExtractableResponse<Response> 신분당선 = LineApi.createLineApi("신분당선", "bg-red-600", 강남역, 정자역, 10);

        // When
        long lineId = 신분당선.jsonPath().getLong("id");
        ExtractableResponse<Response> 신분당선_수정_응답 = LineApi.changeLineByIdApi(lineId, "새로운분당선", "bg-red-700");

        // Then
        ExtractableResponse<Response> 지하철노선_특정_조회_응답 = LineApi.getLineByIdApi(lineId);
        String changedName = 지하철노선_특정_조회_응답.jsonPath().getString("name");
        String changedColor = 지하철노선_특정_조회_응답.jsonPath().getString("color");

        assertAll(
                () -> assertThat(changedName).isEqualTo("새로운분당선"),
                () -> assertThat(changedColor).isEqualTo("bg-red-700")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        // Given
        ExtractableResponse<Response> 신분당선 = LineApi.createLineApi("신분당선", "bg-red-600", 강남역, 정자역, 10);

        // When
        long lineId = 신분당선.jsonPath().getLong("id");
        ExtractableResponse<Response> 신분당선_삭제_응답 = LineApi.deleteLineByIdApi(lineId);

        // Then
        ExtractableResponse<Response> 모든_노선_조회_응답 = LineApi.getAllLinesApi();
        List<Long> lineIds = 모든_노선_조회_응답.jsonPath().getList("id", Long.class);
        assertThat(lineIds).hasSize(0);
    }
}
