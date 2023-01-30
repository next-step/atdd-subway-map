package subway.section.entity;

import org.junit.jupiter.api.Test;
import subway.line.entity.Line;
import subway.station.entity.Station;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    @Test
    void 노선_추가_테스트() {
        //given
        Line line = Line.builder()
                .color("red")
                .name("노선")
                .build();

        Section section = Section.builder()
                .downStation(new Station(1L, "역1"))
                .upStation(new Station(2L, "역2"))
                .distance(1)
                .build();

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

        Section section1 = Section.builder()
                .id(1L)
                .upStation(new Station(1L, "역2"))
                .downStation(new Station(2L, "역1"))
                .distance(1)
                .build();

        Section section2 = Section.builder()
                .id(2L)
                .upStation(new Station(2L, "역2"))
                .downStation(new Station(3L, "역1"))
                .distance(1)
                .build();

        oldLine.addSection(section1);
        oldLine.addSection(section2);

        //when
        section2.changeLine(newLine);

        //then
        assertThat(oldLine.getSections().size()).isEqualTo(1);
        assertThat(oldLine.getLastSection()).isEqualTo(section1);
        assertThat(section2.getLine()).isEqualTo(newLine);
    }

    @Test
    void 노선_구간_삭제_하행역Id로_삭제한다() {
        //given
        Line line = Line.builder()
                .color("red")
                .name("노선")
                .build();

        Section section1 = Section.builder()
                .id(1L)
                .upStation(new Station(1L, "역2"))
                .downStation(new Station(2L, "역1"))
                .distance(1)
                .build();

        Section section2 = Section.builder()
                .id(2L)
                .upStation(new Station(2L, "역2"))
                .downStation(new Station(3L, "역1"))
                .distance(1)
                .build();

        line.addSection(section1);
        line.addSection(section2);

        //when
        line.removeSectionByStationId(3L);

        //then
        assertThat(line.getSections().size()).isEqualTo(1);
        assertThat(line.getLastSection()).isEqualTo(section1);
        assertThat(section2.getLine()).isNull();
    }

    @Test
    void 노선_구간_삭제_구간_엔티티로_삭제한다() {
        //given
        Line line = Line.builder()
                .color("red")
                .name("노선")
                .build();

        Section section1 = Section.builder()
                .id(1L)
                .upStation(new Station(1L, "역2"))
                .downStation(new Station(2L, "역1"))
                .distance(1)
                .build();

        Section section2 = Section.builder()
                .id(2L)
                .upStation(new Station(2L, "역2"))
                .downStation(new Station(3L, "역1"))
                .distance(1)
                .build();

        line.addSection(section1);
        line.addSection(section2);

        //when
        line.remove(section2);

        //then
        assertThat(line.getSections().size()).isEqualTo(1);
        assertThat(line.getLastSection()).isEqualTo(section1);
        assertThat(section2.getLine()).isNull();
    }
}