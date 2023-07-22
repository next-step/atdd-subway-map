package subway.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.StringUtils;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private Long distance;

    @Embedded
    private Sections sections;

    public Line() {}

    public Line(Long id, String name, String color, Long distance, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = sections;
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

    public void modifyTheLine(String name, String color, Long distance, Sections sections) {
        updateName(name);
        updateColor(color);
        updateDistance(distance);
        updateSections(sections);
    }

    private void updateSections(Sections sections) {
        if (sections != null) {
            this.sections.update(sections);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private void updateName(String name) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
    }

    private void updateColor(String color) {
        if (StringUtils.hasText(color)) {
            this.color = color;
        }
    }

    private void updateDistance(Long distance) {
        if (isGreaterThanZero(distance)) {
            this.distance = distance;
        }
    }

    private boolean isGreaterThanZero(Long distance) {
        return distance != null && distance > 0L;
    }

    public void connectNewSection(Station destinationStation, Section section) {
        sections.connect(destinationStation, section);
        updateDistance(sections.getDistance());
    }

    public Station getUpEndStation() {
        return sections.startStation;
    }

    public Station getDownEndStation() {
        return sections.getDownEndStation();
    }

    public boolean containsStation(Station station) {
        return false;
    }

    public void disconnectSection(Station deleteStation) {
        sections.disconnectStation(deleteStation);
        updateDistance(sections.getDistance());
    }

    public static class Builder {
        private Long id;

        private String name;

        private String color;

        private Long distance;

        private Sections sections;

        public Builder id(Long id) {
            this.id = id;
            return this;
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

        public Builder sections(Sections sections) {
            this.sections = sections;
            return this;
        }

        public Line build() {
            return new Line(id, name, color, distance, sections);
        }
    }
}
