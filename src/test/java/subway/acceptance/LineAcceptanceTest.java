package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.util.LineApiRequest.*;
import static subway.util.StationApiRequest.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.exception.LineDuplicationNameException;
import subway.exception.LineNotFoundException;
import subway.exception.SectionDuplicationStationException;
import subway.exception.SectionNotConnectingStationException;
import subway.exception.SectionRemoveLastStationException;
import subway.exception.SectionRemoveSizeException;
import subway.exception.StationNotFoundException;
import subway.util.StationApiRequest;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        StationResponse upStation = 지하철역_리스폰_변환(지하철역_생성_요청(신사역));
        StationResponse downStation = 지하철역_리스폰_변환(지하철역_생성_요청(논현역));

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_이름, 신분당선_색상,
            upStation.getId(), downStation.getId(), 10);

        // then
        지하철_노선_생성됨(response);
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        지하철_노선_목록_조회됨(지하철_노선_목록_조회_요청(), 지하철_노선_리스폰_변환(response));
    }

    @DisplayName("지하철 노선을 생성할 때 중복된 이름이 있으면 실패한다.")
    @Test
    void crateLineFail() {
        // given
        신분당선_생성();

        // when
        ExtractableResponse<Response> response = 신분당선_생성();

        // then
        중복된_이름으로_실패됨(response);
    }

    private void 중복된_이름으로_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getInt("status"))
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(LineDuplicationNameException.message);
    }

    @DisplayName("지하철 노선을 생성할 때 해당 지하철역이 없으면 실패한다.")
    @Test
    void crateLineFail2() {
        // given
        StationResponse upStation = 지하철역_리스폰_변환(지하철역_생성_요청(신사역));

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선_이름, 신분당선_색상,
            upStation.getId(), 2L, 10);

        // then
        지하철역이_존재_하지_않아_실패됨(response);
    }

    private void 지하철역이_존재_하지_않아_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.jsonPath().getInt("status"))
            .isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(StationNotFoundException.message);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());
        LineResponse 경강선 = 지하철_노선_리스폰_변환(경강선_생성());

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회됨(response, 신분당선, 경강선);
    }

    private void 지하철_노선_목록_조회됨(ExtractableResponse<Response> response, LineResponse ... lineResponses) {
        List<LineResponse> responses = response.jsonPath().getList(".", LineResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        IntStream.range(0, responses.size())
            .forEach(i -> 지하철_노선_조회됨(response, responses.get(i), lineResponses[i]));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선.getId());

        // then
        지하철_노선_조회됨(response, 지하철_노선_리스폰_변환(response), 신분당선);
    }

    private void 지하철_노선_조회됨(ExtractableResponse<Response> response, LineResponse actual, LineResponse expected) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("지하철 노선을 조회할 때 해당 지하철 노선이 없으면 실패한다.")
    @Test
    void showLineFail() {
        // given

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(1L);

        // then
        지하철_노선이_존재_하지_않아_실패됨(response);
    }

    private void 지하철_노선이_존재_하지_않아_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.jsonPath().getInt("status"))
            .isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(LineNotFoundException.message);
    }
    
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(신분당선.getId(), 경강선_이름, 경강선_색상);

        // then
        LineResponse expected = new LineResponse(신분당선.getId(), 경강선_이름, 경강선_색상, 신분당선.getStations());
        지하철_노선_수정됨(response, expected);
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response, LineResponse expected) {
        ExtractableResponse<Response> findResponse = 지하철_노선_조회_요청(expected.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        지하철_노선_조회됨(findResponse, 지하철_노선_리스폰_변환(findResponse), expected);
    }

    @DisplayName("지하철 노선을 수정할 때 해당 지하철 노선이 없으면 실패한다.")
    @Test
    void updateLineFail() {
        // given

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(1L, 경강선_이름, 경강선_색상);

        // then
        지하철_노선이_존재_하지_않아_실패됨(response);
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(신분당선.getId());

        // then
        지하철_노선_삭제됨(response, 신분당선);
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response, LineResponse expected) {
        List<String> lineNames = 지하철_노선_목록_조회_요청().jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(lineNames).doesNotContain(expected.getName());
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createSection() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());
        StationResponse upStation = 신분당선.getStations().get(1);
        StationResponse downStation = 지하철역_리스폰_변환(지하철역_생성_요청(신논현역));

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_생성_요청(신분당선.getId(), upStation.getId(),
            downStation.getId(), 10);

        // then
        지하철_노선에_구간_생성됨(response);
    }

    private void 지하철_노선에_구간_생성됨(ExtractableResponse<Response> response) {
        LineResponse expected = 지하철_노선_리스폰_변환(response);
        ExtractableResponse<Response> findResponse = 지하철_노선_조회_요청(expected.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        지하철_노선_조회됨(findResponse, 지하철_노선_리스폰_변환(findResponse), expected);
    }

    @DisplayName("지하철 노선에 구간을 생성할 때 하행 종점역이 새로운 구간의 상행역이 아니면 실패한다.")
    @Test
    void createSectionFail() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());
        StationResponse upStation = 지하철역_리스폰_변환(지하철역_생성_요청(StationApiRequest.신논현역));
        StationResponse downStation = 지하철역_리스폰_변환(지하철역_생성_요청(StationApiRequest.강남역));

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_생성_요청(신분당선.getId(), upStation.getId(),
            downStation.getId(), 10);

        // then
        하행_종점역이_새로운_구간의_상행역이_아니면_실패됨(response);
    }

    private void 하행_종점역이_새로운_구간의_상행역이_아니면_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getInt("status"))
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(SectionNotConnectingStationException.MESSAGE);
    }

    @DisplayName("지하철 노선에 구간을 생성할 때 새로운 구간의 하행역이 중복된 역이라면 실패한다.")
    @Test
    void createSectionFail2() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());
        StationResponse upStation = 신분당선.getStations().get(1);
        StationResponse downStation = 신분당선.getStations().get(0);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_생성_요청(신분당선.getId(), upStation.getId(),
            downStation.getId(), 10);

        // then
        새로운_구간의_하행역이_중복된_역이면_실패됨(response);
    }

    private void 새로운_구간의_하행역이_중복된_역이면_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getInt("status"))
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(SectionDuplicationStationException.MESSAGE);
    }

    @DisplayName("지하철 노선에 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_신사역_논현역_신논현역_구간_생성());
        List<StationResponse> stations = 신분당선.getStations();
        StationResponse removeStation = stations.get(stations.size() - 1);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_삭제_요청(신분당선.getId(),
            removeStation.getId());

        // then
        지하철_노선에_구간이_삭제됨(response, 신분당선.getId(), removeStation.getName());
    }

    private void 지하철_노선에_구간이_삭제됨(ExtractableResponse<Response> response, Long lineId,
        String removeStationName) {
        List<String> lineNames = 지하철_노선_조회_요청(lineId).jsonPath()
            .getList("stations.name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(lineNames).doesNotContain(removeStationName);
    }

    @DisplayName("지하철 노선에 구간을 삭제할 떄 마지막 구간이 아닌 경우 실패한다.")
    @Test
    void deleteSectionFail() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_신사역_논현역_신논현역_구간_생성());
        List<StationResponse> stations = 신분당선.getStations();
        StationResponse removeStation = stations.get(stations.size() - 2);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_삭제_요청(신분당선.getId(),
            removeStation.getId());

        // then
        마지막_구간이_아닌_경우_실패됨(response);
    }

    private void 마지막_구간이_아닌_경우_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getInt("status"))
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(SectionRemoveLastStationException.MESSAGE);
    }

    @DisplayName("지하철 노선에 구간을 삭제할 때 구간이 1개인 경우 실패한다.")
    @Test
    void deleteSectionFail2() {
        // given
        LineResponse 신분당선 = 지하철_노선_리스폰_변환(신분당선_생성());
        List<StationResponse> stations = 신분당선.getStations();
        StationResponse removeStation = stations.get(stations.size() - 1);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_삭제_요청(신분당선.getId(),
            removeStation.getId());

        // then
        구간이_1개인_경우_실패됨(response);
    }

    private void 구간이_1개인_경우_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getInt("status"))
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message"))
            .isEqualTo(SectionRemoveSizeException.MESSAGE);
    }
}
