package nextstep.subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void 거리를_생성한다(long input) {
        // given
        Distance distance = new Distance(input);

        // then
        assertThat(distance.getDistance()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -2L})
    void 거리가_1_미만인_경우_예외를_발생시킨다(long distance) {
        // then
        assertThatIllegalArgumentException().isThrownBy(() -> new Distance(distance));
    }
}