package subway.model;

import subway.exception.AddSectionException;
import subway.exception.ErrorMessage;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Sections sections = new Sections();

    @Column(nullable = false)
    private Long distance;

    protected Line() {
    }

    public Line(String name, String color, Sections sections, Long distance) {
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.distance = distance;
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

    public Long getDistance() {
        return distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        this.distance += distance;
    }

    public void deleteLastSection(Station upStation) {
        Long lastDistance = sections.findLastSectionDistance();
        sections.deleteLastSection(upStation);
        distance -= lastDistance;
    }

    public Section createSection(Station upStation, Station downStation, Long distance) {
        if (!sections.equalToLastDownStation(upStation)) {
            throw new AddSectionException(ErrorMessage.WRONG_UPSTATION_ID);
        }
        if (sections.includes(downStation)) {
            throw new AddSectionException(ErrorMessage.WRONG_DOWNSTATION_ID);
        }

        return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    public List<Section> getSections() {
        return this.sections.getSections();
    }

    public static class Builder {
        private String name;
        private String color;
        private List<Section> sections;
        private Long distance;

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Builder sections(List<Section> sections) {
            this.sections = sections;
            return this;
        }

        public Line build() {
            return new Line(name, color, new Sections(sections), distance);
        }
    }
}
