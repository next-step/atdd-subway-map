package nextstep.subway.section.domain;

import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineTestSource.lineId;
import static nextstep.subway.section.SectionTestSource.section;
import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    @Test
    void duplicated되는Section() {
        final Section section = section(1L);
        final Section newSection = section(1L);

        final boolean result = newSection.isDuplicated(section);

        assertThat(result).isTrue();
    }

    @Test
    void duplicated되지않는Section() {
        final Section section = section(1L);
        final Section newSection = Section.builder()
                .lineId(lineId)
                .upStationId(-1123L)
                .downStationId(-1231L)
                .distance(10L).build();

        final boolean result = newSection.isDuplicated(section);

        assertThat(result).isFalse();
    }

}