package nextstep.subway.domain;

import nextstep.subway.exception.InvalidDownStationException;
import nextstep.subway.exception.InvalidUpStationException;
import nextstep.subway.exception.RemoveSectionFailException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public void change(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        section.setLine(this);
        sections.addSection(section);
    }

    public void removeSection(Long stationId) {
        sections.removeSection(stationId);
    }
}
