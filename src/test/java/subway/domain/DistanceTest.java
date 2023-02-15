package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리 관련 기능")
class DistanceTest {

    @DisplayName("구간에 값을 더한다.")
    @ParameterizedTest(name = "plus : {0}")
    @ValueSource(ints = {3, 6, 9, 100})
    void add(int value) {
        Distance expected = new Distance(5 + value);

        assertThat(new Distance(5).plus(new Distance(value))).isEqualTo(expected);
    }

    @DisplayName("구간에 값을 뺀다.")
    @ParameterizedTest(name = "minus : {0}")
    @ValueSource(ints = {3, 6, 9, 99})
    void minus(int value) {
        Distance expected = new Distance(100 - value);

        assertThat(new Distance(100).minus(new Distance(value))).isEqualTo(expected);
    }

    @DisplayName("구간에 뺀 값이 최소값보다 작을 경우 에러 처리한다.")
    @Test
    void minusValueIsUnderMin() {
        assertThatThrownBy(() -> new Distance(5).minus(new Distance(6))).isInstanceOf(IllegalArgumentException.class);
    }
}
