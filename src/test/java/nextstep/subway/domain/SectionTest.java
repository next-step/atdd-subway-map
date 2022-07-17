package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    @DisplayName("같은 역일 경우 예외")
    void sameStationException() {
        assertThatThrownBy(() ->
            new Section(new Station("강남역"), new Station("강남역"), new Distance(10)))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("두 역이 같은 정보일 수 없습니다.");
    }

    @Test
    @DisplayName("하행 종점역 id가 맞지 않는경우 예외")
    void isNotEqual() {
        final Section section = new Section(new Station(1L, "강남역"), new Station(2L, "양재역"), new Distance(10));
        assertThatThrownBy(() -> section.isEqualTo(3L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("마지막 지하철역 id가 맞지 않습니다.");
    }
}