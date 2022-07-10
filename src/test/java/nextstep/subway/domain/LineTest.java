package nextstep.subway.domain;

import nextstep.subway.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static nextstep.subway.domain.line.Fixture.역정보;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineTest {


    @Test
    @DisplayName("노선은 식별자, 이름, 색, 거리, 역들의 정보를 가진다.")
    void test1() {
        // when & then
        assertDoesNotThrow(
            () -> new Line(null, "신분당선", "bg-red-600", 10, 역정보)
        );
    }


    @ParameterizedTest
    @DisplayName("이름은 2글자 미만이면 IllegalArgumentException 예외를 발생한다.")
    @NullAndEmptySource
    void test2(String name) {
        // given
        String 선 = name;

        // when & then
        assertThatThrownBy(
            () -> new Line(선, "bg-red-600", 10, 역정보)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("색은 4글자 미만이면 IllegalArgumentException 예외를 발생한다.")
    void test3() {
        // given
        String 색 = "600";

        // when & then
        assertThatThrownBy(
            () -> new Line("신분당선", 색, 10, 역정보)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("거리는 1 미만이면 IllegalArgumentException 예외를 발생한다")
    void test4() {
        // given
        int 거리 = 0;

        // when & then
        assertThatThrownBy(
            () -> new Line("신분당선", "bg-red-600", 거리, 역정보)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
