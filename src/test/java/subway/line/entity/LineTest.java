package subway.line.entity;

import org.junit.jupiter.api.Test;
import subway.common.TestFixTure;
import subway.section.entity.Section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.TestFixTure.createSectionFixTrue;

class LineTest {

    @Test
    void 업데이트() {
        Line line = Line.builder()
                .name("노선")
                .color("red")
                .build();

        line.update("line", "blue");

        assertThat(line.getName()).isEqualTo("line");
        assertThat(line.getColor()).isEqualTo("blue");
    }
}