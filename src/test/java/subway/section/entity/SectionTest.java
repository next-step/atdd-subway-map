package subway.section.entity;

import org.junit.jupiter.api.Test;
import subway.line.entity.Line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.TestFixTure.createSectionFixTrue;

class SectionTest {

    @Test
    void 노선_추가_테스트() {
        //given
        Line line = Line.builder()
                .color("red")
                .name("노선")
                .build();

        Section section = createSectionFixTrue();

        //when
        section.changeLine(line);

        //then
        assertThat(section.getLine()).isEqualTo(line);
        assertThat(line.sectionsSize()).isEqualTo(1);
        assertThat(line.getLastSection()).isEqualTo(section);
    }

    @Test
    void 노선의_구간을_변경_한다() {
        //given
        Line oldLine = Line.builder()
                .color("red")
                .name("노선")
                .build();

        Line newLine = Line.builder()
                .color("red")
                .name("노선2")
                .build();

        Section section1 = createSectionFixTrue(1L, 2L);
        Section section2 = createSectionFixTrue(2L, 3L);

        oldLine.addSection(section1);
        oldLine.addSection(section2);

        //when
        section2.changeLine(newLine);

        //then
        assertThat(oldLine.sectionsSize()).isEqualTo(1);
        assertThat(oldLine.getLastSection()).isEqualTo(section1);
        assertThat(section2.getLine()).isEqualTo(newLine);
    }

    @Test
    void 구간에서_노선을_삭제한다() {
        //given
        Line line = Line.builder()
                .color("red")
                .name("노선")
                .build();

        Section section = createSectionFixTrue(1L, 2L);
        Section section2 = createSectionFixTrue(2L, 3L);
        line.addSection(section);
        line.addSection(section2);

        //when
        section2.removeLine();

        //then
        assertThat(line.sectionsSize()).isEqualTo(1);
        assertThat(line.getLastSection()).isEqualTo(section);
        assertThat(section2.getLine()).isNull();
    }
}