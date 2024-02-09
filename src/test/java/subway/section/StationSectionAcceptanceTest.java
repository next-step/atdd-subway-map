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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import utils.StationLineManager;
import utils.StationManager;
import utils.StationSectionManager;

@DisplayName("지하철역 구간 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StationSectionAcceptanceTest {

    @BeforeEach
    void setUp() {

        StationManager.save("A"); // 1
        StationManager.save("C"); // 2
        StationManager.save("B"); // 3
        StationLineManager.save("신분당선", "bg-red-600", 1L, 2L, 7L);  // A - C
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
        StationSectionRequest requestDto = new StationSectionRequest(2L, 3L, 3L); // C - B 구간

        ExtractableResponse<Response> response = StationSectionManager.save(1L, requestDto); // 노선에 구간 추가

        // then
        List<Long> result = response.jsonPath().getList("stations");
        Assertions.assertThat(result.size()).isEqualTo(3);  // A - C - B
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
        StationSectionManager.saveFailure(1L, requestDto);
    }

    /**
     * Given 지하철 노선을 생성하고 A역 - C역 - B역 구간을 등록한다.
     * When stationId에 해당하는 역을 제거한다.
     * Then A역 - C역 순으로 구간이 등록되어 있다.
     * Exception
     * 1. 노선에 포함된 역이 한 개인 경우
     * 2. 제거하려는 역의 번호가 노선의 하행역이 아닌 경우
     */
    // TODO: 지하철 구간 제거 인수 테스트 메서드 생성
    @DisplayName("지하철 구간을 제거한다.")
    @Test
    void deleteStationSection() {
        // when
        StationSectionRequest requestDto = new StationSectionRequest(2L, 3L, 3L); // C - B 구간
        StationSectionManager.save(1L, requestDto); // 노선에 구간 추가

        // 구간 제거
        StationSectionManager.remove(1L, 3L);

        // 조회
        ExtractableResponse<Response> response = StationLineManager.findById(1L);

        // then
        List<Long> result = response.jsonPath().getList("stations");
        Assertions.assertThat(result.size()).isEqualTo(2);  // A - C
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
        StationSectionManager.removeFailure(1L, stationId);
    }

    private static Stream<Arguments> invalidSaveStationSectionParameters() {
        return Stream.of(
                Arguments.of(new StationSectionRequest(2L, 1L, 3L)), // (A - C) - (C - A) // 이미 등록된 역(A)
                Arguments.of(new StationSectionRequest(1L, 3L, 3L))  // A - C - (A - B) // 상행역이 하행역이 아닐 때
        );
    }
}
