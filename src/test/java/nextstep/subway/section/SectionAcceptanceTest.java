package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineAcceptanceRequest.지하철_노선_생성_요청;
import static nextstep.subway.section.SectionAcceptanceAssertion.지하철_구간_등록_실패됨;
import static nextstep.subway.section.SectionAcceptanceAssertion.지하철_구간_등록됨;
import static nextstep.subway.section.SectionAcceptanceRequest.지하철_구간_등록_요청;
import static nextstep.subway.station.StationAcceptanceRequest.지하철_역_생성_요청;


@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private StationResponse 상행역;
    private StationResponse 하행역;
    private StationResponse 신규역;

    @BeforeEach
    public void initialize() {
        // given
        상행역 = 지하철_역_생성_요청(new StationRequest("상행역")).as(StationResponse.class);
        하행역 = 지하철_역_생성_요청(new StationRequest("하행역")).as(StationResponse.class);
        신규역 = 지하철_역_생성_요청(new StationRequest("신규역")).as(StationResponse.class);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void postSection() {
        // given
        Long lineId = 지하철_노선_생성_요청(new LineRequest("1호선", "blue", 상행역.getId(), 하행역.getId(), 10))
                .as(LineResponse.class)
                .getId();

        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(lineId, new SectionRequest(하행역.getId(), 신규역.getId(), 25));

        // then
        지하철_구간_등록됨(response);
    }

    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void postSectionWithNonRegisteredStation() {
        // given
        Long lineId = 지하철_노선_생성_요청(new LineRequest("1호선", "blue", 상행역.getId(), 하행역.getId(), 10))
                .as(LineResponse.class)
                .getId();

        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(lineId, new SectionRequest(상행역.getId(), 신규역.getId(), 25));

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.")
    @Test
    void postInvalidSection() {
        // given
        Long lineId = 지하철_노선_생성_요청(new LineRequest("1호선", "blue", 상행역.getId(), 하행역.getId(), 10))
                .as(LineResponse.class)
                .getId();

        // when
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(lineId, new SectionRequest(하행역.getId(), 상행역.getId(), 25));

        // then
        지하철_구간_등록_실패됨(response);
    }
}
