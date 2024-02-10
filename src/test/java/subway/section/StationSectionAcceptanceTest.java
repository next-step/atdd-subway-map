package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.E2ETestInitializer;
import subway.line.StationLineRequest;
import subway.line.StationLineResponse;
import utils.line.StationLineManager;
import utils.section.StationSectionManager;
import utils.station.StationManager;

@DisplayName("지하철역 구간 관리 기능")
public class StationSectionAcceptanceTest extends E2ETestInitializer {

    public static final long DISTANCE = 10L;
    public static final int ALL_STATIONS_COUNT = 3;
    public static final int ONE_REMOVE_STATIONS_COUNT = 2;
    public static final String FIRST_LINE_NAME = "신분당선";
    public static final String FIRST_LINE_COLOR = "bg-red-600";

    private static StationLineResponse savedLine;
    private static StationLineRequest saveLineRequest;

    private static long savedFirstStationId;
    private static long savedSecondStationId;
    private static long savedThirdStationId;

    @BeforeEach
    void setUp() {
        savedFirstStationId = StationManager.save("지하철역").jsonPath().getLong("id");
        savedSecondStationId = StationManager.save("새로운지하철역").jsonPath().getLong("id");
        savedThirdStationId = StationManager.save("또다른지하철역").jsonPath().getLong("id");

        saveLineRequest = new StationLineRequest(FIRST_LINE_NAME, FIRST_LINE_COLOR, savedFirstStationId, savedSecondStationId, DISTANCE);
        savedLine = StationLineManager.save(saveLineRequest).as(StationLineResponse.class);  // A - C
    }

    /**
     * Given 지하철 노선을 생성하고 여기에 A역과 C역을 생성한다.
     * When B역을 등록한다.
     * Then A역 - C역 - B역 순으로 구간이 등록되어 있다.
     */
    // TODO: 지하철 구간 등록 인수 테스트 메서드 생성
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void createStationSection() {
        // when
        StationSectionRequest requestDto = new StationSectionRequest(savedSecondStationId, savedThirdStationId, DISTANCE); // C - B 구간

        ExtractableResponse<Response> response = StationSectionManager.save(savedLine.getId(), requestDto); // 노선에 구간 추가

        // then
        List<Long> result = response.jsonPath().getList("stations");
        Assertions.assertThat(result.size()).isEqualTo(ALL_STATIONS_COUNT);  // A - C - B
    }

    /**
     * Exception
     * 1. 등록하려는 역이 이미 등록되어 있는 경우
     * 2. 등록하려는 역의 상행역이 현재 노선의 하행역이 아닌 경우
     *
     */
    @DisplayName("잘못된 구간을 등록하면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("invalidSaveStationSectionParameters")
    void invalidCreateStationSection(StationSectionRequest requestDto) {
        // given
        // 현재 노선: A(1) - C(2)

        // when
        StationSectionManager.saveFailure(savedLine.getId(), requestDto);
    }

    /**
     * Given 지하철 노선을 생성하고 A역 - C역 - B역 구간을 등록한다.
     * When B역을 제거한다.
     * Then A역 - C역 순으로 구간이 등록되어 있다.
     */
    // TODO: 지하철 구간 제거 인수 테스트 메서드 생성
    @DisplayName("지하철 구간을 제거한다.")
    @Test
    void deleteStationSection() {
        // when
        StationSectionRequest requestDto = new StationSectionRequest(savedSecondStationId, savedThirdStationId, DISTANCE); // C - B 구간
        StationSectionManager.save(savedLine.getId(), requestDto); // 노선에 구간 추가

        // 구간 제거
        StationSectionManager.remove(savedLine.getId(), savedThirdStationId);

        // 조회
        ExtractableResponse<Response> response = StationLineManager.findById(savedLine.getId());

        // then
        List<Long> result = response.jsonPath().getList("stations");
        Assertions.assertThat(result.size()).isEqualTo(ONE_REMOVE_STATIONS_COUNT); // A - C
    }

    /**
     * Exception
     * 1. 제거하려는 역이 마지막 구간의 하행 종점역이 아닐 경우
     * 2. 구간이 1개인 경우
     */
    // TODO: 지하철 구간 제거 예외 테스트 메서드 생성
    @DisplayName("잘못된 구간을 제거하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void invalidDeleteStationSection(long stationId) {
        // given
        // 현재 노선: A(1) - C(2)

        // when
        StationSectionManager.removeFailure(savedLine.getId(), stationId);
    }

    private static Stream<Arguments> invalidSaveStationSectionParameters() {
        return Stream.of(
                Arguments.of(new StationSectionRequest(savedSecondStationId, savedFirstStationId, DISTANCE)), // (A - C) - (C - A) // 이미 등록된 역(A)
                Arguments.of(new StationSectionRequest(savedFirstStationId, savedThirdStationId, DISTANCE))  // A - C - (A - B) // 상행역이 하행역이 아닐 때
        );
    }
}
