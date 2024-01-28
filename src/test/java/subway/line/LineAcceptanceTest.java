package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.testhelper.LineApiCaller;
import subway.testhelper.StationApiCaller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@Sql({"/test-sql/table-truncate.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private Map<String, String> newBunDangLineParams;
    private Map<String, String> zeroLineParams;
    private final StationApiCaller stationApiCaller = new StationApiCaller();
    private final LineApiCaller lineApiCaller = new LineApiCaller();

    @BeforeEach
    void setUpClass() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        Long firstId = stationApiCaller.callCreateStation(params).jsonPath().getObject("id", Long.class);
        params.put("name", "삼성역");
        Long secondId = stationApiCaller.callCreateStation(params).jsonPath().getObject("id", Long.class);
        params.put("name", "선릉역");
        Long thirdId = stationApiCaller.callCreateStation(params).jsonPath().getObject("id", Long.class);

        newBunDangLineParams = new HashMap<>();
        newBunDangLineParams.put("name", "신분당선");
        newBunDangLineParams.put("color", "bg-red-600");
        newBunDangLineParams.put("upStationId", firstId.toString());
        newBunDangLineParams.put("downStationId", secondId.toString());
        newBunDangLineParams.put("distance", "10");

        zeroLineParams = new HashMap<>();
        zeroLineParams.put("name", "0호선");
        zeroLineParams.put("color", "bg-red-100");
        zeroLineParams.put("upStationId", firstId.toString());
        zeroLineParams.put("downStationId", thirdId.toString());
        zeroLineParams.put("distance", "10");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = lineApiCaller.callApiCreateLines(newBunDangLineParams);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        response = lineApiCaller.callApiFindLines();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> actual = response.jsonPath().getList("name", String.class);
        String expected = "신분당선";
        assertThat(actual).containsAnyOf(expected);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선들의 목록을 조회한다.")
    @Test
    void findLines() {
        // given
        ExtractableResponse<Response> response = lineApiCaller.callApiCreateLines(newBunDangLineParams);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        response = lineApiCaller.callApiCreateLines(zeroLineParams);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        response = lineApiCaller.callApiFindLines();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> actual = response.jsonPath().getList("name", String.class);

        // then
        List<String> expected = List.of("신분당선", "0호선");
        int actualSize = actual.size();
        int expectedSize = 2;
        assertThat(actual).isEqualTo(expected);
        assertThat(actualSize).isEqualTo(expectedSize);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void findLine() {
        // given
        ExtractableResponse<Response> response = lineApiCaller.callApiCreateLines(newBunDangLineParams);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.header("location");

        // when
        response = lineApiCaller.callApiFindLine(location);

        // then
        String actual = response.jsonPath().getObject("name", String.class);
        String expected = "신분당선";
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> response = lineApiCaller.callApiCreateLines(newBunDangLineParams);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.header("location");

        // when
        LineUpdateRequest request = new LineUpdateRequest("다른분당선", "bg-red-600");
        response = lineApiCaller.callApiUpdateLine(request, location);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        response = lineApiCaller.callApiFindLine(location);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);
        String expectedName = "다른분당선";
        String expectedColor = "bg-red-600";
        assertThat(actual.getName()).isEqualTo(expectedName);
        assertThat(actual.getColor()).isEqualTo(expectedColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> response = lineApiCaller.callApiCreateLines(newBunDangLineParams);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.header("location");

        // when
        response = lineApiCaller.callApiDeleteLine(location);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        response = lineApiCaller.callApiFindLines();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> actual = response.jsonPath().getList(".", LineResponse.class);
        List<LineResponse> expected = Collections.emptyList();
        assertThat(actual).containsAll(expected);
    }

}
