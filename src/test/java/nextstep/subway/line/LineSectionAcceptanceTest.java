package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static nextstep.subway.line.LineRequestSteps.지하철_노선_생성_요청;
import static nextstep.subway.station.StationRequestSteps.지하철_역_등록_됨;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철 노선에 구간을 등록한다.")
    void addLineSection() {
        // given
        StationResponse 강남역 = 지하철_역_등록_됨("강남역").as(StationResponse.class);
        StationResponse 판교역 = 지하철_역_등록_됨("판교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 판교역.getId(), 5);
        LineResponse 신분당선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);

        // when
        StationResponse 광교역 = 지하철_역_등록_됨("광교역").as(StationResponse.class);
        ExtractableResponse<Response> response = 지하철_노선에_지하철_역_등록_요청(신분당선, 판교역, 광교역, 9);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        지하철_노선에_지하철_역_등록_됨(response);
        지하철_노선에_지하철_역_순서_정렬됨(response, Arrays.asList(강남역, 판교역, 광교역));
    }
}
