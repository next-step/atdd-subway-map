package subway.acceptance.line;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.acceptance.AcceptanceTest;
import subway.acceptance.station.StationFixture;
import subway.line.service.request.LineCreateRequest;
import subway.line.service.request.SectionRequest;

@DisplayName("지하철구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {
    private final StationFixture stationFixture = new StationFixture();
    private final LineFixture lineFixture = new LineFixture();
    private final SectionFixture sectionFixture = new SectionFixture();

    private Long 논현역, 신논현역, 강남역, 양재역;

    @BeforeEach
    public void setUp() {
        논현역 = stationFixture.지하철역을_생성한다("논현역").getId();
        신논현역 = stationFixture.지하철역을_생성한다("신논현역").getId();
        강남역 = stationFixture.지하철역을_생성한다("강남역").getId();
        양재역 = stationFixture.지하철역을_생성한다("양재역").getId();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 등록하면
     * Then 등록된 구간을 응답받을 수 있다.
     */
    @DisplayName("지하철 노선에 구간을 등록한다")
    @Test
    void appendSection() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                논현역,
                신논현역,
                10L
        );

        // given
        final var lineId = lineFixture.지하철노선을_생성한다(request).getId();

        // when
        final var response = sectionFixture.지하철구간을_등록한다(lineId, new SectionRequest(신논현역, 강남역, 10L));

        // then
        assertThat(response.getStations()).hasSize(3);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 등록할때
     * When 새로운 구간의 상행역이 노선의 하행 종점역이 아니라면
     * Then 새로운 구간을 등록할 수 없다
     */
    @DisplayName("지하철 구간 등록에 실패한다 - 노선의 하행역과 구간의 상행역이 일치하지 않은 경우")
    @Test
    void appendFail1() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                논현역,
                신논현역,
                10L
        );

        // given
        final var lineId = lineFixture.지하철노선을_생성한다(request).getId();

        // when & then
        sectionFixture.지하철구간_등록에_실패한다(lineId, new SectionRequest(강남역, 양재역, 10L));
        assertThat(lineFixture.지하철노선을_조회한다(lineId).getStations()).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 등록할때
     * When 새로운 구간의 하행역이 노선에 등록되어 있는 역이라면
     * Then 새로운 구간을 등록할 수 없다
     */
    @DisplayName("지하철 구간 등록에 실패한다 - 구간의 하행역이 이미 노선에 등록되어 있는 경우")
    @Test
    void appendFail2() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                신논현역,
                강남역,
                10L
        );

        // given
        final var lineId = lineFixture.지하철노선을_생성한다(request).getId();

        // when & then
        sectionFixture.지하철구간_등록에_실패한다(lineId, new SectionRequest(강남역, 신논현역, 10L));
        assertThat(lineFixture.지하철노선을_조회한다(lineId).getStations()).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 지하철 구간을 등록하고
     * When 등록한 지하철 구간을 삭제하면
     * Then 해당 지하철 구간 정보는 삭제된다
     */
    @DisplayName("지하철 노선에서 구간을 삭제한다")
    @Test
    void removeSection() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                신논현역,
                강남역,
                10L
        );

        // given
        final var lineId = lineFixture.지하철노선을_생성한다(request).getId();
        sectionFixture.지하철구간을_등록한다(lineId, new SectionRequest(강남역, 양재역, 10L));

        // when
        sectionFixture.지하철구간을_제거한다(lineId, 양재역);

        // then
        assertThat(lineFixture.지하철노선을_조회한다(lineId).getStations()).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 지하철 구간을 등록하고
     * When 등록한 지하철 구간을 삭제할때
     * When 삭제하고자 하는 구간이 마지막 구간이 아니라면
     * Then 해당 지하철 구간 정보는 삭제할 수 없다
     */
    @DisplayName("지하철 구간 제거에 실패한다 - 삭제하고자 하는 구간이 마지막 구간이 아닌 경우")
    @Test
    void removeFail1() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                신논현역,
                강남역,
                10L
        );

        // given
        final var lineId = lineFixture.지하철노선을_생성한다(request).getId();
        sectionFixture.지하철구간을_등록한다(lineId, new SectionRequest(강남역, 양재역, 10L));

        // when & then
        sectionFixture.지하철구간_제거에_실패한다(lineId, 강남역);
        assertThat(lineFixture.지하철노선을_조회한다(lineId).getStations()).hasSize(3);
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 지하철 구간을 등록하고
     * When 등록한 지하철 구간을 삭제할때
     * When 노선에 상행 종점역과 하행 종점역만 있는 경우
     * Then 해당 지하철 구간 정보는 삭제할 수 없다
     */
    @DisplayName("지하철 구간 제거에 실패한다 - 노선에 상행 종점역과 하행 종점역만 있는 경우")
    @Test
    void removeFail2() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                신논현역,
                강남역,
                10L
        );

        // given
        final var lineId = lineFixture.지하철노선을_생성한다(request).getId();

        // when & then
        sectionFixture.지하철구간_제거에_실패한다(lineId, 강남역);
        assertThat(lineFixture.지하철노선을_조회한다(lineId).getStations()).hasSize(2);
    }
}
