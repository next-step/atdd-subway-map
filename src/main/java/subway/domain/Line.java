package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToOne(cascade = CascadeType.ALL)
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

    public Line updateName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        return this;
    }

    public Line updateColor(String color) {
        if (color != null && !color.isBlank()) {
            this.color = color;
        }
        return this;
    }

    public Line updateEndStations(EndStations endStations) {
        if (endStations != null) {
            this.endStations = endStations;
        }
        return this;
    }

    public Line updateDistance(Long distance) {
        if (distance != null && distance > 0) {
            this.distance = distance;
        }
        return this;
    }

    @Override
    public String toString() {
        return "Line{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", color='" + color + '\'' +
            ", stations=" + endStations +
            ", distance=" + distance +
            '}';
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
