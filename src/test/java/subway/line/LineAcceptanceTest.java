package subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import subway.station.StationResponse;

import static subway.AssertionUtils.목록은_다음을_포함한다;
import static subway.line.LineApi.*;
import static subway.line.LineAssertion.*;
import static subway.line.LineFixture.LINE_UPDATE_REQUEST;
import static subway.station.StationApi.지하철역_생성;
import static subway.station.StationFixture.*;


@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    private LineCreateRequest lineA;
    private LineCreateRequest lineB;

    @BeforeEach
    void setUp() {
        final StationResponse stationA = 지하철역_생성(STATION_A).as(StationResponse.class);
        final StationResponse stationB = 지하철역_생성(STATION_B).as(StationResponse.class);
        final StationResponse stationC = 지하철역_생성(STATION_C).as(StationResponse.class);
        final StationResponse stationD = 지하철역_생성(STATION_D).as(StationResponse.class);

        lineA = new LineCreateRequest("line-A", "line-A-color", stationA.getId(), stationB.getId(), 10);
        lineB = new LineCreateRequest("line-B", "line-B-color", stationC.getId(), stationD.getId(), 8);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLineTest() {
        // given
        지하철노선_생성(lineA);

        // when & then
        목록은_다음을_포함한다(지하철노선_이름_목록_조회(), lineA.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLinesTest() {
        // given
        지하철노선_생성(lineA);
        지하철노선_생성(lineB);

        // when
        목록은_다음을_포함한다(지하철노선_이름_목록_조회(), lineA.getName(), lineB.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 단건 조회 한다.")
    @Test
    void getLineTest() {
        // given
        final LineResponse 생성한_지하철노선 = 지하철노선_생성후_응답객체반환(lineA);

        // when
        final LineResponse 조회된_지하철노선 = 지하철노선_단건_조회후_응답객체반환(생성한_지하철노선.getId());

        // then
        생성한_지하철노선과_조회된_지하철노선은_같다(생성한_지하철노선, 조회된_지하철노선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLineTest() {
        // given
        final LineResponse 생성한_지하철노선 = 지하철노선_생성후_응답객체반환(lineA);

        // when
        지하철노선_수정(생성한_지하철노선.getId(), LINE_UPDATE_REQUEST);
        final LineResponse 수정후_조회된_지하철노선 = 지하철노선_단건_조회후_응답객체반환(생성한_지하철노선.getId());

        // then
        지하철노선_수정후_지하철노선의_이름과_색상이_바뀌어있다(수정후_조회된_지하철노선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLineTest() {
        // given
        final LineResponse 생성한_지하철노선 = 지하철노선_생성후_응답객체반환(lineA);

        // when
        지하철노선_삭제(생성한_지하철노선.getId());

        // then
        지하철노선_삭제후_목록조회하면_찾을_수_없다(생성한_지하철노선);
    }
}
