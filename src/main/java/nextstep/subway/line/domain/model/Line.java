package nextstep.subway.line.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.common.domain.model.BaseEntity;
import nextstep.subway.station.domain.model.Station;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String color;

    @Embedded
    private Sections sections;

    protected Line() {
        this.sections = new Sections();
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
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

    public void edit(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean matchSectionsSize(int size) {
        return this.sections.size() == size;
    }

    public boolean notMatchName(String name) {
        return !this.name.equals(name);
    }

    public Section addSection(Station upStation, Station downStation, Distance distance) {
        return sections.add(this, upStation, downStation, distance);
    }

    public void deleteSection(Long sectionId) {
        sections.delete(sectionId);
    }

    public List<Station> stations() {
        return sections.toStations();
    }
}
