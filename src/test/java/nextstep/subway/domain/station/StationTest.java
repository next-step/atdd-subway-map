package nextstep.subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class StationTest {

    @Test
    @DisplayName("역은 식별자와 이름을 가진다.")
    void test1() {
        // when & then
        assertDoesNotThrow(
            () -> new Station(1L, "가천역")
        );
    }

    @DisplayName("이름은 2글자 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(strings = "역")
    @NullAndEmptySource
    void test2(String name) {
        // given
        String 역 = name;

        //when & then
        assertThatThrownBy(
            () -> new Station(역)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
