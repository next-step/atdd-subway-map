package subway.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.controller.dto.LineRequest.UpdateRequest;
import subway.exception.LineNotEstablishedBySameEndStationException;

@Entity
public class Line {

    public static final int VALID_LINE_STATION_COUNT = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany
    private Set<Station> stations;

    private Long distance;

    public Line() {}

    public Line(Long id, String name, String color, Set<Station> stations, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = valid(stations);
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

    public Set<Station> getStations() {
        return Set.copyOf(stations);
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

    private Set<Station> valid(Set<Station> stations) {
        if (isCountNotValid(stations)) {
            throw new LineNotEstablishedBySameEndStationException();
        }
        return stations;
    }

    private boolean isCountNotValid(Set<Station> stations) {
        return stations.size() != VALID_LINE_STATION_COUNT;
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
            this.stations = valid(request.getEndStations());
        }
    }

    private void updateDistance(UpdateRequest request) {
        if (request.hasDistance()) {
            this.distance = request.getDistance();
        }
    }

    @Override
    public String toString() {
        return "Line{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", color='" + color + '\'' +
            ", stations=" + stations +
            ", distance=" + distance +
            '}';
    }

    public static class Builder {
        private Long id;

        private String name;

        private String color;

        private Set<Station> stations;

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

        public Builder stations(Set<Station> stations) {
            this.stations = Set.copyOf(stations);
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(id, name, color, stations, distance);
        }
    }
}
