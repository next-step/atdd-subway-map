package nextstep.subway.line;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineRequestSteps.지하철_노선_생성_요청;
import static nextstep.subway.station.StationRequestSteps.지하철_역_등록_됨;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private LineResponse 신분당선;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철_역_등록_됨("강남역").as(StationResponse.class);
        양재역 = 지하철_역_등록_됨("양재역").as(StationResponse.class);

        LineRequest 신분당선_노선_생성_요청 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 7);
        LineResponse 신분당선 = 지하철_노선_생성_요청(신분당선_노선_생성_요청).as(LineResponse.class);
    }

    @Test
    @DisplayName("지하철 노선에 구간을 등록한다.")
    void addLineSection() {
        // when
        // 새로운 구간의 하행선
        StationResponse 양재시민의숲역 = 지하철_역_등록_됨("양재시민의숲역").as(StationResponse.class);
        // TODO : 기존 노선의 하행성을 구해서 상행선으로 사용 -> 신분당선.getDownStation()
        StationResponse 양재역 = 신분당선.getDownStation();

        // TODO : 신분당선에 기존 노선의 하행선을 상행선으로, 새로운 역을 하행선으로 설정
        // TODO : 신분당 선에 구간 추가 요청을 보내서 구간 추가
        ExtractableResponse<Response> response = 지하철_노선에_새로운_구간_등록_요청(신분당선, 양재역.getId(), 양재시민의숲역.getId(), 5);

        // then
        지하철_노선에_새로운_구간_등록됨(response);
    }
}
