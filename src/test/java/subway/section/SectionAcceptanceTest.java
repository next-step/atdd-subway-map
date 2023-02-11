package subway.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.line.SectionRequest;
import subway.station.StationResponse;

import static subway.AssertionUtils.목록은_다음을_정확하게_포함한다;
import static subway.line.LineApi.지하철노선_단건_조회후_응답객체반환;
import static subway.line.LineApi.지하철노선_생성;
import static subway.section.SectionApi.지하철구간_등록;
import static subway.station.StationApi.지하철역_생성;
import static subway.station.StationFixture.*;

@DisplayName("구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private StationResponse stationA;
    private StationResponse stationB;
    private StationResponse stationC;
    private StationResponse stationD;

    private LineResponse lineA;

    private SectionRequest sectionA;

    @BeforeEach
    void setUp() {
        stationA = 지하철역_생성(STATION_A).as(StationResponse.class);
        stationB = 지하철역_생성(STATION_B).as(StationResponse.class);
        stationC = 지하철역_생성(STATION_C).as(StationResponse.class);
        stationD = 지하철역_생성(STATION_D).as(StationResponse.class);

        lineA = 지하철노선_생성(new LineCreateRequest("line-A", "line-A-color", stationA.getId(), stationB.getId(), 10)).as(LineResponse.class);

        sectionA = new SectionRequest(stationC.getId(), stationB.getId(), 5);
    }

    /**
     * given: 지하철 노선의 하행 종점역을 상행역으로 갖는 구간을 등록하고
     * when: 지하철 노선을 조회하면
     * then: 등록한 구간의 역들이 상행역 하행역 순서로 있는 것을 확인할 수 있다.
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addSectionTest() {
        // given
        지하철구간_등록(lineA.getId(), sectionA);

        // when
        final LineResponse lineResponse = 지하철노선_단건_조회후_응답객체반환(lineA.getId());

        // then
        목록은_다음을_정확하게_포함한다(lineResponse.getStations(), stationA, stationB, stationC);
    }
}
