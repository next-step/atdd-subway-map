package nextstep.subway.section.domain;

import org.junit.jupiter.api.Test;

import static nextstep.subway.section.SectionTestSource.section;
import static nextstep.subway.section.SectionTestSource.sections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionsTest {

    @Test
    void section이연결가능함() {
        final Section section = section(1L, 2L);
        final Sections sections = sections(section);

        final boolean result = sections.isConnectable(section(2L, 3L));

        assertThat(result).isTrue();
    }

    @Test
    void section이연결가능하지않음() {
        final Section section = section(1L, 2L);
        final Sections sections = sections(section);

        final boolean result = sections.isConnectable(section(10L, 21L));

        assertThat(result).isFalse();
    }

    @Test
    void section이순환함() {
        final Section section = section(1L, 2L);
        final Sections sections = sections(section);

        final boolean result = sections.hasCircularSection(section(2L, 1L));

        assertThat(result).isTrue();
    }

    @Test
    void section이순환하지않음() {
        final Section section1 = section(1L, 2L);
        final Sections sections = sections(section1);

        final boolean result = sections.hasCircularSection(section(2L, 3L));

        assertThat(result).isFalse();
    }

    @Test
    void 상행종점역조회() {
        final Section section1 = section(1L, 2L);
        final Sections sections = sections(section1);

        final Long result = sections.getFirstUpStationId();

        assertThat(result).isEqualTo(1L);
    }

    @Test
    void 하행종점역조회() {
        final Section section1 = section(1L, 2L);
        final Sections sections = sections(section1);

        final Long result = sections.getLastDownStationId();

        assertThat(result).isEqualTo(2L);
    }

    @Test
    void 구간삭제불가능_구간이1개임() {
        final Section section1 = section(1L, 2L);
        final Sections sections = sections(section1);

        final IllegalStateException result = assertThrows(
                IllegalStateException.class,
                () -> sections.removeLastSection(section1.getDownStationId()));

        assertThat(result).hasMessageContaining("해당 역을 삭제할 수 없습니다.");
    }

    @Test
    void 구간삭제불가능_하행종점역이아님() {
        final Section section1 = section(1L, 2L);
        final Sections sections = sections(section1);

        final Section section2 = section(2L, 3L);
        sections.addSection(section2);

        final IllegalStateException result = assertThrows(
                IllegalStateException.class,
                () -> sections.removeLastSection(section1.getDownStationId()));

        assertThat(result).hasMessageContaining("해당 역을 삭제할 수 없습니다.");
    }

    @Test
    void 구간삭제가능() {
        final Section section1 = section(1L, 2L);
        final Sections sections = sections(section1);

        final Section section2 = section(2L, 3L);
        sections.addSection(section2);

        sections.removeLastSection(section2.getDownStationId());

        assertThat(sections.getLastDownStationId()).isEqualTo(section1.getDownStationId());
    }

}