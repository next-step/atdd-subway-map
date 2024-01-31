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
        LineCreateRequest 이호선 = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        ExtractableResponse<Response> response = lineApiRequester.createLineApiCall(이호선);

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
        LineCreateRequest 이호선 = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        Long 이호선id = JsonPathUtil.getId(lineApiRequester.createLineApiCall(이호선));

        LineCreateRequest 일호선 = new LineCreateRequest(
                "1호선",
                "blue",
                3L,
                4L,
                10
        );
        Long 일호선id = JsonPathUtil.getId(lineApiRequester.createLineApiCall(일호선));

        //when
        ExtractableResponse<Response> response = lineApiRequester.findLinesApiCall();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(JsonPathUtil.getIds(response)).containsExactly(이호선id, 일호선id);
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
        LineCreateRequest 이호선 = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        Long 이호선id = JsonPathUtil.getId(lineApiRequester.createLineApiCall(이호선));

        //when
        ExtractableResponse<Response> response = lineApiRequester.findLineApiCall(이호선id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(JsonPathUtil.getId(response)).isEqualTo(이호선id);
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
        LineCreateRequest 이호선 = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        Long 이호선id = JsonPathUtil.getId(lineApiRequester.createLineApiCall(이호선));

        //when
        LineUpdateRequest 일호선 = new LineUpdateRequest("1호선", "blue");
        ExtractableResponse<Response> response = lineApiRequester.updateLineApiCall(이호선id, 일호선);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> findLineResponse = lineApiRequester.findLineApiCall(이호선id);
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
        LineCreateRequest 이호선 = new LineCreateRequest(
                "2호선",
                "green",
                1L,
                2L,
                10
        );
        Long 이호선id = JsonPathUtil.getId(lineApiRequester.createLineApiCall(이호선));

        //when
        ExtractableResponse<Response> response = lineApiRequester.deleteLineApiCall(이호선id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(lineApiRequester.findLineApiCall(이호선id).asPrettyString()).isEqualTo("존재하지 않는 노선입니다.");
    }
}
