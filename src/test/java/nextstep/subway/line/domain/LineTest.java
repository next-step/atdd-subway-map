package nextstep.subway.line.domain;

import nextstep.subway.section.domain.Section;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineTestSource.lineWithSection;
import static nextstep.subway.section.SectionTestSource.section;
import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    void section이연결가능함() {
        final Line line = lineWithSection();
        final Section section = section(line.getLastDownStationId());

        final boolean result = line.isConnectable(section);

        assertThat(result).isTrue();
    }

    @Test
    void section이연결가능하지않음() {
        final Line line = lineWithSection();
        final Section section = section(2020L);

        final boolean result = line.isConnectable(section);

        assertThat(result).isFalse();
    }

    @Test
    void section이순환함() {
        final Line line = lineWithSection();
        final Section section = section(line.getLastDownStationId());

        final boolean result = line.hasCircularSection(section);

        assertThat(result).isTrue();
    }

    @Test
    void section이순환하지않음() {
        final Line line = lineWithSection();
        final Section section = section(line.getLastDownStationId(), 2022L);

        final boolean result = line.hasCircularSection(section);

        assertThat(result).isFalse();
    }

    @Test
    void 상행종점역조회() {
        final Line line = lineWithSection();

        final Long result = line.getFirstUpStationId();

        assertThat(result).isEqualTo(8L);
    }

    @Test
    void 하행종점역조회() {
        final Line line = lineWithSection();

        final Long result = line.getLastDownStationId();

        assertThat(result).isEqualTo(9L);
    }

}