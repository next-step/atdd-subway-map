package nextstep.subway.line.domain;

import nextstep.subway.section.domain.Section;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineTestSource.line;
import static nextstep.subway.section.SectionTestSource.section;
import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    void section이연결가능함() {
        final Line line = line();
        final Section section = section(line.getDownStationId());

        final boolean result = line.isConnectableSection(section);

        assertThat(result).isTrue();
    }

    @Test
    void section이연결가능하지않음() {
        final Line line = line();
        final Section section = section(2020L);

        final boolean result = line.isConnectableSection(section);

        assertThat(result).isFalse();
    }



}