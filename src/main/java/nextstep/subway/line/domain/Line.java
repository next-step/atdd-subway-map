package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    public Line(String name, String color, Section firstSection) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(firstSection);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void appendSection(Section section) {
        sections.appendSection(section);
    }

    public void removeStation(Station station) {
        sections.removeStation(station);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections.getSections());
    }
}
