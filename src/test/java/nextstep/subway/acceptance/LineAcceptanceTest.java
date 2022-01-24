package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step_feature.LineStepFeature;
import nextstep.subway.acceptance.step_feature.StationStepFeature;
import nextstep.subway.applicaion.dto.ShowLineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static nextstep.subway.acceptance.step_feature.LineStepFeature.NUMBER2_LINE_NAME;
import static nextstep.subway.acceptance.step_feature.LineStepFeature.SHINBUNDANG_LINE_NAME;
import static nextstep.subway.acceptance.step_feature.StationStepFeature.GANGNAM_STATION_NAME;
import static nextstep.subway.acceptance.step_feature.StationStepFeature.YEOKSAM_STATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        Map<String, String> params = LineStepFeature.createShinbundangLineParams();

        // when
        ExtractableResponse<Response> response = LineStepFeature.callCreateLines(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank()
                .isEqualTo("/lines/1");
    }

    /**
     * Given 노선을 생성한다.
     * When 같은 이름의 지하철 노선 생성을 요청 하면
     * Then 400 status code를 응답한다.
     */
    @DisplayName("중복된 이름의 지하철 노선 생성은 실패한다")
    @Test
    void createLine_duplicate_fail() {
        // given
        Map<String, String> params = LineStepFeature.createShinbundangLineParams();
        LineStepFeature.callCreateLines(params);

        // when
        ExtractableResponse<Response> response = LineStepFeature.callCreateLines(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        Map<String, String> shinbundangLine = LineStepFeature.createShinbundangLineParams();
        Map<String, String> number2Line = LineStepFeature.createNumber2LineParams();
        LineStepFeature.callCreateLines(shinbundangLine);
        LineStepFeature.callCreateLines(number2Line);

        // when
        ExtractableResponse<Response> response = LineStepFeature.callGetLines();

        // then
        List<String> lineNames = response.jsonPath()
                .getList(".", ShowLineResponse.class)
                .stream()
                .map(ShowLineResponse::getLineName)
                .collect(toList());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).contains(SHINBUNDANG_LINE_NAME, NUMBER2_LINE_NAME);

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        Map<String, String> params = LineStepFeature.createShinbundangLineParams();
        LineStepFeature.callCreateLines(params);

        // when
        ExtractableResponse<Response> response = LineStepFeature.callGetLines(1);

        // then
        String lineName = response.jsonPath()
                .getString("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
        assertThat(lineName).contains(SHINBUNDANG_LINE_NAME);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        StationResponse gangnam = StationStepFeature.callCreateAndFind(GANGNAM_STATION_NAME);
        StationResponse yeoksam = StationStepFeature.callCreateAndFind(YEOKSAM_STATION_NAME);
        Map<String, String> param = LineStepFeature.createLineParams(NUMBER2_LINE_NAME, "green", gangnam.getId(), yeoksam.getId(), 10);
        ShowLineResponse lineResponse = LineStepFeature.callCreateAndFind(param);

        // when
        param.put("id", String.valueOf(lineResponse.getLineId()));
        param.put("name", "구분당선");
        param.put("color", "blue");

        // when
        ExtractableResponse<Response> responseUpdate = LineStepFeature.callUpdateLines(param);

        // then
        ExtractableResponse<Response> response = LineStepFeature.callGetLines();
        List<String> lineNames = response.jsonPath()
                .getList(".", ShowLineResponse.class)
                .stream()
                .map(ShowLineResponse::getLineName)
                .collect(toList());

        assertThat(responseUpdate.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(lineNames).contains("구분당선");
        assertThat(lineNames).doesNotContain(SHINBUNDANG_LINE_NAME);
    }

    /**
     * When 없는 지하철 노선의 정보 수정을 요청 하면
     * Then 400 응답
     */
    @DisplayName("지하철 노선 수정 요청 시 노선을 못 찾으면 400응답 처리")
    @Test
    void updateLine_fail() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("name", "구분당선");
        params.put("color", "blue");

        // when
        ExtractableResponse<Response> response = LineStepFeature.callUpdateLines(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        Map<String, String> shinbundangLine = LineStepFeature.createShinbundangLineParams();
        LineStepFeature.callCreateLines(shinbundangLine);

        // when
        ExtractableResponse<Response> response = LineStepFeature.callDeleteLines(1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
