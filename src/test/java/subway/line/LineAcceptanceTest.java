package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.TableTruncate;
import subway.station.StationApiRequester;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineUpdateRequest;
import subway.util.JsonPathUtil;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private TableTruncate tableTruncate;

    private final StationApiRequester stationApiRequester = new StationApiRequester();
    private final LineApiRequester lineApiRequester = new LineApiRequester();

    @BeforeEach
    void setUp() {
        tableTruncate.truncate();

        stationApiRequester.createStationApiCall("잠실역");
        stationApiRequester.createStationApiCall("용산역");
        stationApiRequester.createStationApiCall("건대입구역");
        stationApiRequester.createStationApiCall("성수역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        //when
        LineCreateRequest request = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        ExtractableResponse<Response> response = lineApiRequester.createLineApiCall(request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(JsonPathUtil.getIds(lineApiRequester.findLinesApiCall()))
                .containsExactly(JsonPathUtil.getId(response));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void showLiens() {
        //given
        LineCreateRequest requestA = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        Long savedIdA = JsonPathUtil.getId(lineApiRequester.createLineApiCall(requestA));

        LineCreateRequest requestB = new LineCreateRequest(
                "1호선",
                "blue",
                3L,
                4L,
                10
        );
        Long savedIdB = JsonPathUtil.getId(lineApiRequester.createLineApiCall(requestB));

        //when
        ExtractableResponse<Response> response = lineApiRequester.findLinesApiCall();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(JsonPathUtil.getIds(response)).containsExactly(savedIdA, savedIdB);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void showLine() {
        //given
        LineCreateRequest request = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        Long savedId = JsonPathUtil.getId(lineApiRequester.createLineApiCall(request));

        //when
        ExtractableResponse<Response> response = lineApiRequester.findLineApiCall(savedId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(JsonPathUtil.getId(response)).isEqualTo(savedId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void updateLine() {
        //given
        LineCreateRequest createRequest = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        Long id = JsonPathUtil.getId(lineApiRequester.createLineApiCall(createRequest));

        //when
        LineUpdateRequest request = new LineUpdateRequest("1호선", "blue");
        ExtractableResponse<Response> response = lineApiRequester.updateLineApiCall(id, request);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> findLineResponse = lineApiRequester.findLineApiCall(id);
        assertThat(JsonPathUtil.getString(findLineResponse, "name")).isEqualTo("1호선");
        assertThat(JsonPathUtil.getString(findLineResponse, "color")).isEqualTo("blue");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteLine() {
        //given
        LineCreateRequest request = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        Long id = JsonPathUtil.getId(lineApiRequester.createLineApiCall(request));

        //when
        ExtractableResponse<Response> response = lineApiRequester.deleteLineApiCall(id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(lineApiRequester.findLineApiCall(id).asPrettyString()).isEqualTo("존재하지 않는 노선입니다.");
    }
}
