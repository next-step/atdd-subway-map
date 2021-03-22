package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.LineTestStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.SectionTestStep.*;
import static nextstep.subway.station.StationTestStep.지하철_역_목록_등록되어_있음;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    List<StationResponse> stationResponseList;
    StationResponse 강남역;
    StationResponse 역삼역;
    StationResponse 광교역;

    @BeforeEach
    void setup() {
        stationResponseList = 지하철_역_목록_등록되어_있음();
        강남역 = stationResponseList.get(0);
        광교역 = stationResponseList.get(1);
        역삼역 = stationResponseList.get(2);
    }

    @DisplayName("노선에 구간을 생성한다.")
    @Test
    void createSection() {
        // given
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red", 강남역.getId(), 광교역.getId(), 1);

        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(addedLineResponse.getId(), 광교역.getId(), 역삼역.getId(), 1);

        // then
        지하철_구간_등록_확인(response);
    }

    @DisplayName("노선에 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        LineResponse addedLineResponse = 지하철_노선_등록되어_있음("신분당선", "red", 강남역.getId(), 광교역.getId(), 1);
        지하철_구간_등록_요청(addedLineResponse.getId(), 광교역.getId(), 역삼역.getId(), 1);

        // when
        ExtractableResponse<Response> response = 지하철_구간_삭제_요쳥(addedLineResponse.getId(), 역삼역.getId());

        // then
        지하철_구간_삭제_확인(response);
    }
}
