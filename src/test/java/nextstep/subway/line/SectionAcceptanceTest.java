package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.LineTestStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.SectionTestStep.지하철_구간_등록_요청;
import static nextstep.subway.line.SectionTestStep.지하철_구간_등록_확인;
import static nextstep.subway.station.StationTestStep.지하철_역_목록_등록되어_있음;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("노선에 구간을 생성한다.")
    @Test
    void createSection() {
        // given
        List<StationResponse> stationResponseList = 지하철_역_목록_등록되어_있음();
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red", stationResponseList.get(0).getId(), stationResponseList.get(1).getId(), 1);

        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(addedLineResponse.getId(), stationResponseList.get(1).getId(), stationResponseList.get(2).getId(), 1);

        // then
        지하철_구간_등록_확인(response);
    }

}
