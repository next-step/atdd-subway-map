package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationTestStep;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.LineTestStep.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        List<StationResponse> stationResponseList = StationTestStep.지하철_역_목록_등록되어_있음();
        ExtractableResponse response = 지하철_노선_생성_요청("신분당선", "red", stationResponseList.get(0).getId(), stationResponseList.get(1).getId(), 1);

        // then
        지하철_노선_생성_확인(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        List<StationResponse> stationResponseList = StationTestStep.지하철_역_목록_등록되어_있음();
        List<LineResponse> lineResponseList = 지하철_노선_목록_등록되어_있음(stationResponseList);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_확인(lineResponseList, response);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        List<StationResponse> stationResponseList = StationTestStep.지하철_역_목록_등록되어_있음();
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red", stationResponseList.get(0).getId(), stationResponseList.get(1).getId(), 1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(addedLineResponse.getId());

        // then
        지하철_노선_확인(addedLineResponse, response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        List<StationResponse> stationResponseList = StationTestStep.지하철_역_목록_등록되어_있음();
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red", stationResponseList.get(0).getId(), stationResponseList.get(1).getId(), 1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청("2호선", "green", addedLineResponse.getId(), stationResponseList.get(0).getId(), stationResponseList.get(1).getId(), 1);

        // then
        지하철_노선_수정_확인(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        List<StationResponse> stationResponseList = StationTestStep.지하철_역_목록_등록되어_있음();
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red", stationResponseList.get(0).getId(), stationResponseList.get(1).getId(), 1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(addedLineResponse.getId());

        // then
        지하철_노선_삭제_확인(response);
    }

    @DisplayName("기존에 존재하는 노선 이름 으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        //given
        List<StationResponse> stationResponseList = StationTestStep.지하철_역_목록_등록되어_있음();
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red", stationResponseList.get(0).getId(), stationResponseList.get(1).getId(), 1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "yellow", stationResponseList.get(0).getId(), stationResponseList.get(1).getId(), 1);

        // then
        지하철_노선_생성_실패_확인(response);
    }
}
