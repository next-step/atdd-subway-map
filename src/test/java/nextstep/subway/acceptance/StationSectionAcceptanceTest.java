package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.SubwayApiCaller.지하철구간_등록;
import static nextstep.subway.acceptance.SubwayApiCaller.지하철구간_삭제;
import static nextstep.subway.acceptance.SubwayApiCaller.지하철노선_조회;
import static nextstep.subway.acceptance.SubwayApiCaller.지하철역_노선_등록;
import static nextstep.subway.acceptance.SubwayApiCaller.지하철역_등록;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.applicaion.dto.StationSectionResponse;
import nextstep.subway.common.BaseAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 기능")
public class StationSectionAcceptanceTest extends BaseAcceptanceTest {

    private ExtractableResponse<Response> 신분당선;
    private Long 강남역;
    private Long 역삼역;
    private Long 선릉역;

    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_등록("강남역").as(StationResponse.class).getId();
        역삼역 = 지하철역_등록("역삼역").as(StationResponse.class).getId();
        선릉역 = 지하철역_등록("역삼역").as(StationResponse.class).getId();

        신분당선 = 지하철역_노선_등록("신분당선", "bg-red-600", 강남역, 역삼역, 10);

    }

    /**
     * When 지하철 구간을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 구간을 찾을 수 있다.
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void createStationSection() {
        //when
        String 신분당선_저장_위치 = 신분당선.header("Location");
        지하철구간_등록(신분당선_저장_위치,역삼역,선릉역,7);

        //then
        StationLineResponse stationLineResponses = 지하철노선_조회(신분당선_저장_위치);

        assertThat(stationLineResponses.getName()).isEqualTo("신분당선");
        assertThat(stationLineResponses.getStations()).containsExactly(
                new StationResponse(강남역,"강남역"),
                new StationResponse(역삼역,"역삼역"),
                new StationResponse(선릉역,"선릉역"));
    }
}
