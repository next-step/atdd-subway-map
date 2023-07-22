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

    @Embedded
    private EndStations endStations;

    private Long distance;

    public Line() {}

    public Line(Long id, String name, String color, EndStations stations, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.endStations = stations;
        this.distance = distance;
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

    public EndStations getEndStations() {
        return endStations.clone();
    }

    public Long getDistance() {
        return distance;
    }

    public void modifyTheLine(String name, String color, EndStations endStations, Long distance) {
        updateName(name);
        updateColor(color);
        updateEndStations(endStations);
        updateDistance(distance);
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

    private void updateEndStations(EndStations endStations) {
        if (Objects.nonNull(endStations)) {
            this.endStations.update(endStations);
        }
    }

    private void updateDistance(Long distance) {
        if (isGreaterThanZero(distance)) {
            this.distance = distance;
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

    @Override
    public String toString() {
        return "Line{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", color='" + color + '\'' +
            ", endStations=" + endStations +
            ", distance=" + distance +
            '}';
    }

    private boolean isGreaterThanZero(Long distance) {
        return distance != null && distance > 0L;
    }

    public static class Builder {
        private Long id;

        private String name;

        private String color;

        private EndStations endStations;

        private Long distance;

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

        public Builder stations(EndStations stations) {
            this.endStations = stations.clone();
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(id, name, color, endStations, distance);
        }
    }
}
