package subway.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import subway.controller.dto.LineRequest.UpdateRequest;

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

    public void updateNewInfo(UpdateRequest request) {
        updateName(request);
        updateColor(request);
        updateEndStations(request);
        updateDistance(request);
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

    private void updateName(UpdateRequest request) {
        if (request.hasName()) {
            this.name = request.getName();
        }
    }

    private void updateColor(UpdateRequest request) {
        if (request.hasColor()) {
            this.color = request.getColor();
        }
    }

    private void updateEndStations(UpdateRequest request) {
        if (request.hasEndStations()) {
            this.endStations = request.getEndStations();
        }
    }

    private void updateDistance(UpdateRequest request) {
        if (request.hasDistance()) {
            this.distance = request.getDistance();
        }
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
