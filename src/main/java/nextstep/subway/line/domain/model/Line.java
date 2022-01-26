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

    private Line(String name, String color, Station upStation, Station downStation, Distance distance) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
        addSection(upStation, downStation, distance);
    }

    public static Builder builder() {
        return new Builder();
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

    public Section addSection(Station upStation, Station downStation, Distance distance) {
        return sections.add(this, upStation, downStation, distance);
    }

    public void deleteSection(Long sectionId) {
        sections.delete(sectionId);
    }

    public List<Station> getStations() {
        return sections.toStations();
    }

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private Distance distance;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(Distance distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(name, color, upStation, downStation, distance);
        }
    }
}
