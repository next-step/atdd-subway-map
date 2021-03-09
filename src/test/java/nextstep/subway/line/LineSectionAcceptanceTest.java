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
    private ExtractableResponse<Response> 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        삼성역 = 지하철역_생성_요청("삼성역").as(StationResponse.class);
        잠실역 = 지하철역_생성_요청("잠실역").as(StationResponse.class);
        강변역 = 지하철역_생성_요청("강변역").as(StationResponse.class);
        이호선정보 = new LineRequest("2호선", "green", 강남역.getId(), 삼성역.getId(), 10);
        이호선 = 지하철_노선_생성_요청(이호선정보);
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createSection() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 잠실역, 20);

        // then
        지하철_구간_생성됨(response);
    }

    @DisplayName("구간 등록시 상행역은 현재 등록되어 있는 하행 종점역이 아닐 경우 실패됨")
    @Test
    void validateUpStationRegist() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선, 강남역, 잠실역, 30);

        // then
        지하철_구간_생성_실패됨(response);
    }

    @DisplayName("구간 등록시 하행역이 현재 등록되어 있을 경우 실패됨")
    @Test
    void validateDownStationRegist() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 강남역, 5);

        // then
        지하철_구간_생성_실패됨(response);
    }
}
