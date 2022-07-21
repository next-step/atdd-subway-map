package nextstep.subway.domain;

import nextstep.subway.acceptance.util.GivenUtils;
import nextstep.subway.exception.InvalidDistanceValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionTest {

    @Test
    @DisplayName("유효하지 않은 거리 값 - 0보다 작은 수")
    void invalidDistance() {
        // given
        int distance = -1;
        Line line = GivenUtils.이호선();
        Station 강남역 = GivenUtils.강남역();
        Station 역삼역 = GivenUtils.역삼역();

        // when
        Executable executable = () -> new Section(line, 강남역, 역삼역, distance);

        // then
        assertThrows(InvalidDistanceValueException.class, executable);
    }

    @Test
    @DisplayName("유효한 거리 값 - 0보다 같거나 큰 수")
    void validDistance() {
        // given
        int distance = 10;
        Line line = GivenUtils.이호선();
        Station 강남역 = GivenUtils.강남역();
        Station 역삼역 = GivenUtils.역삼역();

        // when
        Section section = new Section(line, 강남역, 역삼역, distance);

        // then
        assertThat(section.getDistance()).isEqualTo(distance);
    }

}