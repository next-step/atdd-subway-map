package nextstep.subway.domain.entity;

import nextstep.subway.domain.service.Validator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(final String name,
                final String color,
                final Station upStation,
                final Station downStation,
                final int distance,
                final Validator<Line> lineValidator) {
        this.name = name;
        this.color = color;

        lineValidator.validate(this);

        addSection(new Section(this, upStation, downStation, distance));
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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void change(final String name, final String color, final Validator<Line> lineValidator) {
        this.name = name;
        this.color = color;

        lineValidator.validate(this);
    }

    public void addSection(final Section section) {
        this.sections.add(section);
    }

    public void removeSection(final Station station) {
        this.sections.remove(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
