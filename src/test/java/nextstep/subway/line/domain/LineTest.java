package nextstep.subway.line.domain;

import nextstep.subway.section.domain.Section;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineTestSource.line;
import static nextstep.subway.line.LineTestSource.lineWithSection;
import static nextstep.subway.section.SectionTestSource.section;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void 구간삭제불가능_구간이1개임() {
        final Line line = lineWithSection();

        final IllegalStateException result = assertThrows(
                IllegalStateException.class,
                () -> line.removeLastSection(line.getLastDownStationId()));

        assertThat(result).hasMessageContaining("해당 역을 삭제할 수 없습니다.");
    }

    @Test
    void 구간삭제불가능_하행종점역이아님() {
        final Line line = line();
        final Section section1 = section(200L, 201L);
        final Section section2 = section(201L, 202L);
        line.addSection(section1);
        line.addSection(section2);

        final IllegalStateException result = assertThrows(
                IllegalStateException.class,
                () -> line.removeLastSection(section1.getDownStationId()));

        assertThat(result).hasMessageContaining("해당 역을 삭제할 수 없습니다.");
    }

    @Test
    void 구간삭제가능() {
        final Line line = line();
        final Section section1 = section(200L, 201L);
        final Section section2 = section(201L, 202L);
        line.addSection(section1);
        line.addSection(section2);

        line.removeLastSection(section2.getDownStationId());

        assertThat(line.getLastDownStationId()).isEqualTo(section1.getDownStationId());
    }

}