package nextstep.subway.domain;

import nextstep.subway.acceptance.fixture.LineFixtures;
import nextstep.subway.acceptance.fixture.StationFixtures;
import nextstep.subway.exception.InvalidDistanceValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static nextstep.subway.acceptance.fixture.ColorFixtures.GREEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionTest {

    @Test
    @DisplayName("유효하지 않은 거리 값 - 0보다 작은 수")
    void invalidDistance() {
        // given
        int distance = -1;
        Line 이호선 = new Line(LineFixtures.이호선.getValue(), GREEN.getValue());
        Station 강남역 = new Station(StationFixtures.강남역.getValue());
        Station 역삼역 = new Station(StationFixtures.역삼역.getValue());

        // when
        Executable executable = () -> new Section(이호선, 강남역, 역삼역, distance);

        // then
        assertThrows(InvalidDistanceValueException.class, executable);
    }

    @Test
    @DisplayName("유효한 거리 값 - 0보다 같거나 큰 수")
    void validDistance() {
        // given
        int distance = 10;
        Line 이호선 = new Line(LineFixtures.이호선.getValue(), GREEN.getValue());
        Station 강남역 = new Station(StationFixtures.강남역.getValue());
        Station 역삼역 = new Station(StationFixtures.역삼역.getValue());

        // when
        Section section = new Section(이호선, 강남역, 역삼역, distance);

        // then
        assertThat(section.getDistance()).isEqualTo(distance);
    }

}