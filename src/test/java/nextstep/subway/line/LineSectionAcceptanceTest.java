package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.utils.LineSteps.*;
import static nextstep.subway.utils.StationSteps.지하철역_생성_요청;

public class LineSectionAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 삼성역;
    private StationResponse 잠실역;
    private StationResponse 강변역;
    private LineRequest 이호선정보;
    private ExtractableResponse<Response> lineResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        삼성역 = 지하철역_생성_요청("삼성역").as(StationResponse.class);
        잠실역 = 지하철역_생성_요청("잠실역").as(StationResponse.class);
        강변역 = 지하철역_생성_요청("강변역").as(StationResponse.class);
        이호선정보 = new LineRequest("2호선", "green", 강남역.getId(), 삼성역.getId(), 10);
        lineResponse = 지하철_노선_생성_요청(이호선정보);
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createSection() {
        // given
        LineRequest 신구간 = 지하철_노선_구간_셋팅(삼성역.getId(), 잠실역.getId(), 20);

        // when
        ExtractableResponse<Response> response
                = 지하철_구간_생성_요청(신구간, lineResponse);

        // then
        지하철_구간_생성됨(response);
    }

    @DisplayName("구간 등록시 상행역은 현재 등록되어 있는 하행 종점역이 아닐 경우 실패됨")
    @Test
    void validateUpStationRegist() {
        // given
        LineRequest 잘못된구간 = 지하철_노선_구간_셋팅(강남역.getId(), 잠실역.getId(), 20);

        // when
        ExtractableResponse<Response> response
                = 지하철_구간_생성_요청(잘못된구간, lineResponse);

        // then
        지하철_구간_생성_실패됨(response);
    }

    @DisplayName("구간 등록시 하행역이 현재 등록되어 있을 경우 실패됨")
    @Test
    void validateDownStationRegist() {
        // given
        LineRequest 잘못된구간 = 지하철_노선_구간_셋팅(삼성역.getId(), 강남역.getId(), 20);

        // when
        ExtractableResponse<Response> response
                = 지하철_구간_생성_요청(잘못된구간, lineResponse);

        // then
        지하철_구간_생성_실패됨(response);
    }
}
