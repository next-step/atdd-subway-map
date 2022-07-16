package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    private Line(String name, String color, Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line create(String name, String color, Section section) {
        Line line = new Line(name, color, Sections.create());
        line.addSection(section);
        return line;
    }

    public void addSection(Section section) {
        sections.add(section);
        section.setLine(this);
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public void changeNameAndColor(String name, String color) {
        if (!this.name.equals(name)) {
            this.name = name;
        }

        if (!this.color.equals(color)) {
            this.color = color;
        }
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String color() {
        return color;
    }

    public List<Station> stations() {
        return sections.stations();
    }
}
