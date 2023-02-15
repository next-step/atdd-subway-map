package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
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
}
