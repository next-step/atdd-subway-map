package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @Embedded
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        final Section section = new Section(this, upStationId, downStationId, distance);
        final List<Section> sections = new ArrayList<>();
        sections.add(section);
        this.sections = Sections.of(sections);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return sections.getUpStationId();
    }

    public Long getDownStationId() {
        return sections.getDownStationId();
    }

    public Long getDistance() {
        return sections.getTotalDistance();
    }

    public Line update(Line line) {
        if (line.getName() != null) {
            name = line.getName();
        }
        if (line.getColor() != null) {
            color = line.getColor();
        }
        return this;
    }

    public void addSection(Section section) {
        sections.add(section);
    }
}
