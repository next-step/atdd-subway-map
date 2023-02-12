package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 목록 관련 기능")
class SectionsTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 정자역;
    private Line line;

    @BeforeEach
    void setUp() {
        this.강남역 = new Station("강남역");
        this.역삼역 = new Station("역삼역");
        this.정자역 = new Station("정자역");
        this.line = new Line("2호선", "bg-red-500", 강남역, 역삼역, 10);
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void add() {
        Sections sections = new Sections();
        Section expected = new Section(10, 역삼역, 정자역, line);

        sections.add(expected);

        assertThat(sections.getSections()).contains(expected);
    }

    @DisplayName("구간 목록을 가져온다.")
    @Test
    void get() {
        Section section = new Section(10, 역삼역, 정자역, line);
        Sections sections = new Sections(section);

        assertThat(sections.getSections()).hasSize(1).contains(section);
    }

    @DisplayName("구간을 제거한다.")
    @Test
    void remove() {
        Section section = new Section(10, 역삼역, 정자역, line);
        Sections sections = new Sections(section);

        sections.remove(section);

        assertThat(sections.getSections()).doesNotContain(section);
    }

    @DisplayName("구간 목록의 상행역을 가져온다.")
    @Test
    void getDownStation() {
        Section section1 = new Section(10, 강남역, 역삼역, line);
        Section section2 = new Section(10, 역삼역, 정자역, line);
        Sections sections = new Sections();
        sections.add(section1);
        sections.add(section2);

        Station actual = sections.getUpStation();

        Assertions.assertAll(
                () -> assertThat(actual).isEqualTo(강남역),
                () -> assertThat(actual).isNotEqualTo(역삼역),
                () -> assertThat(actual).isNotEqualTo(정자역)
        );
    }
}
