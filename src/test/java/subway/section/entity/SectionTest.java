package subway.section.entity;

import org.junit.jupiter.api.Test;
import subway.line.entity.Line;
import subway.station.entity.Station;
import subway.util.RandomUtil;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(oldLine.getSections().size()).isEqualTo(1);
        assertThat(oldLine.getLastSection()).isEqualTo(section1);
        assertThat(section2.getLine()).isEqualTo(newLine);
    }

    public static Section createSectionFixTrue() {
        return createSectionFixTrue(RandomUtil.getLandomLong(), RandomUtil.getLandomLong());
    }

    public static Section createSectionFixTrue(Long upStationId, Long downStationId) {
        return Section.builder()
                .id(RandomUtil.getLandomLong())
                .upStation(new Station(upStationId, "역2"))
                .downStation(new Station(downStationId, "역1"))
                .distance(RandomUtil.getLandomLong())
                .build();
    }
}