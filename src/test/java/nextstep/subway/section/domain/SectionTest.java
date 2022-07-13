package nextstep.subway.section.domain;

import org.junit.jupiter.api.Test;

import static nextstep.subway.section.SectionTestSource.section;
import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    @Test
    void duplicated되는Section() {
        final Section section = section(10L, 11L);
        final Section newSection = section(11L, 10L);

        final boolean result = newSection.isDuplicated(section);

        assertThat(result).isTrue();
    }

    @Test
    void duplicated되지않는Section() {
        final Section section = section(10L, 11L);
        final Section newSection = section(11L, 12L);

        final boolean result = newSection.isDuplicated(section);

        assertThat(result).isFalse();
    }

}