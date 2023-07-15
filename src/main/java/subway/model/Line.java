package subway.model;

import subway.exception.DeleteSectionException;
import subway.exception.ErrorMessage;

import javax.persistence.*;
import java.util.ArrayList;
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

    @ManyToMany
    @JoinTable(
            name = "line_station",
            joinColumns = @JoinColumn(name = "line_id"),
            inverseJoinColumns = @JoinColumn(name = "station_id")
    )
    private List<Station> stations = new ArrayList<>();

    @Column(nullable = false)
    private Long distance;

    protected Line() {
    }

    public Line(String name, String color, List<Station> stations, Long distance) {
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean equalToDownStation(Station upStation) {
        return stations.get(stations.size() - 1).equals(upStation);
    }

    public void addSection(Section section) {
        this.stations.add(section.getDownStation());
        this.distance += distance;
    }

    public boolean includes(Station downStation) {
        return stations.contains(downStation);
    }

    public void deleteLastSection(Section lastSection) {
        if (stations.size() == 2) {
            throw new DeleteSectionException(ErrorMessage.CANNOT_REMOVE_ONLY_ONE_SECTION);
        }
        Station upStation = stations.get(stations.size() - 2);
        Station downStation = stations.get(stations.size() - 1);
        if (!lastSection.equals(upStation, downStation)) {
            throw new DeleteSectionException(ErrorMessage.IS_NOT_LAST_SECTION);
        }
        stations.remove(downStation);
        distance -= lastSection.getDistance();
    }

    public static class Builder {
        private String name;
        private String color;
        private List<Station> stations;
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

        public Builder stations(List<Station> stations) {
            this.stations = stations;
            return this;
        }

        public Line build() {
            return new Line(name, color, stations, distance);
        }
    }
}
