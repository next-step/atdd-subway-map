package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.data.Lines;
import nextstep.subway.data.Stations;
import nextstep.subway.line.LineStep;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationStep;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 기능 관련")
public class SectionAcceptanceTest extends AcceptanceTest {

    private LineResponse 사호선;
    private StationResponse 금정역;
    private StationResponse 범계역;
    private StationResponse 평촌역;
    private StationResponse 인덕원역;


    @BeforeEach
    void setup() {
        super.setUp();

        금정역 = StationStep.지하철역_생성_요청(Stations.금정역).as(StationResponse.class);
        범계역 = StationStep.지하철역_생성_요청(Stations.범계역).as(StationResponse.class);
        평촌역 = StationStep.지하철역_생성_요청(Stations.평촌역).as(StationResponse.class);
        인덕원역 = StationStep.지하철역_생성_요청(Stations.인덕원역).as(StationResponse.class);

        Lines.사호선.put("upStationId", 금정역.getId() + "");
        Lines.사호선.put("downStationId", 범계역.getId() + "");
        Lines.사호선.put("distance", 10 + "");

        사호선 = LineStep.지하철_노선_등록(Lines.사호선).as(LineResponse.class);


    }

    @DisplayName("지하철 노선 구간 등록")
    @Test
    void createSection() {

        //when
        //지하철_노선_구간_등록
        SectionRequest 금정_범계 = SectionRequest.Builder()
                .upStationId(범계역.getId())
                .downStationId(평촌역.getId())
                .distance(10)
                .build();

        ExtractableResponse<Response> response = SectionStep.지하철_노선_구간_등록(사호선.getId(), 금정_범계);

        //then
        //등록된_구간_상행역_하행_종점역_일치함
        SectionStep.등록된_구간_상행역_하행_종점역_일치함(response);
    }

    @DisplayName("지하철 노선 구간 등록시 하행역을 현재 등록되어있는 역으로 한다.")
    @Test
    void createSectionWithRegisteredStation() {
        //given
        //지하철_노선_생성_요청
        //지하철역_생성_요청
        //지하철역_생성_요청
        //지하철역_생성_요청
        //지하철_노선_구간_등록

        //when
        //지하철_노선_구간_등록

        //then
        //지하철_노선_구간_등록_실패됨

    }
}
