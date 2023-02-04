package subway.line;

import static common.CommonResponseBodyExtraction.getList;
import static common.CommonResponseBodyExtraction.getObject;
import static common.CommonRestAssured.delete;
import static common.CommonRestAssured.get;
import static common.CommonRestAssured.post;
import static common.CommonRestAssured.put;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.AbstractAcceptanceTest;
import subway.station.StationAcceptanceTest;
import subway.station.StationResponse;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AbstractAcceptanceTest {

    private static final String BASE_URL = "lines/";
    private static final String 신분당선 = "신분당선";
    private static final String RED = "be-red-600";

    private LineRequest 신분당선_생성_요청_dto() {
        StationResponse 강남역 = StationAcceptanceTest.createStation("강남역");
        StationResponse 미금역 = StationAcceptanceTest.createStation("미금역");

        return new LineRequest(신분당선, RED, 강남역.getId(), 미금역.getId(), 10L);
    }

    private LineResponse 신분당선_생성() {
        ExtractableResponse<Response> response = post(BASE_URL, 신분당선_생성_요청_dto());
        return getObject(response, "$", LineResponse.class);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = post(BASE_URL, 신분당선_생성_요청_dto());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getList(get(BASE_URL), "name")).containsAnyOf(신분당선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        List<LineResponse> lines = List.of(신분당선_생성(), 신분당선_생성());

        // when
        ExtractableResponse<Response> response = get(BASE_URL);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getList(response, "$")).hasSameSizeAs(lines);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        LineResponse 신분당선 = 신분당선_생성();

        // when
        ExtractableResponse<Response> response = get(BASE_URL + 신분당선.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        LineResponse line = getObject(response, "$", LineResponse.class);
        assertThat(line.getName()).isEqualTo(신분당선.getName());
        assertThat(line.getColor()).isEqualTo(신분당선.getColor());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse 신분당선 = 신분당선_생성();

        // when
        LineUpdateRequest request = new LineUpdateRequest("다른분당선", "bg-green-600");
        ExtractableResponse<Response> response = put(BASE_URL + 신분당선.getId(), request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        LineResponse line = findLineById(신분당선.getId());
        assertThat(line.getName()).isEqualTo(request.getName());
        assertThat(line.getColor()).isEqualTo(request.getColor());
    }

    private LineResponse findLineById(Long id) {
        ExtractableResponse<Response> response = get(BASE_URL + id);
        return getObject(response, "$", LineResponse.class);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse 신분당선 = 신분당선_생성();

        // when
        ExtractableResponse<Response> response = delete(BASE_URL + 신분당선.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
