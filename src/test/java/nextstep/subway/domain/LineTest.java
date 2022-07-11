package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private Section section;

    @BeforeEach
    void setUp() {
        section = Section.create(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION, 10);
    }

    @Test
    @DisplayName("노선 정보를 변경하면 변경한 정보로 변경된다.")
    void changeLineContent() {
        Line line = Line.create("신분당선", "bg-red-600", section);

        line.changeNameAndColor("2호선", "bg-green-600");

        assertThat(line.name()).isEqualTo("2호선");
        assertThat(line.color()).isEqualTo("bg-green-600");
    }
}