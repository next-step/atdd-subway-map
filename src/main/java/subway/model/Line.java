package subway.model;

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

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    @Column(nullable = false)
    private Long distance;

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        stations.add(upStation);
        stations.add(downStation);
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

    public void update(Station downStation, Long distance) {
        this.stations.add(downStation);
        this.distance += distance;
    }

    public boolean includes(Station downStation) {
        return stations.contains(downStation);
    }

    public void deleteLastSection(Section section) {
        int lastIndex = sections.size() - 1;
        if (sections.get(lastIndex).equals(section)) {
            sections.remove(lastIndex);
        }
    }

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
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

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(name, color, upStation, downStation, distance);
        }
    }
}
