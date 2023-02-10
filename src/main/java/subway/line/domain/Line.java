package subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import subway.line.section.domain.Section;
import subway.station.domain.Station;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 50, nullable = false)
    private String color;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", referencedColumnName = "id")
    private Station upStation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", referencedColumnName = "id")
    private Station downStation;
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Station> stations = new ArrayList<>();
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();
    @Column(nullable = false)
    private Long distance;

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Line(String name) {
        this.name = name;
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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void changeLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void changeDownStation(Station station) {
        this.downStation = station;
    }

    public void addStation(Station station) {
        this.stations.add(station);

        if (station.getLine() != this) {
            station.setLine(this);
        }
    }

    public void addSection(Section section) {
        this.sections.add(section);

        if (section.getLine() != this) {
            section.setLine(this);
        }
    }

    public boolean hasStation(Station station) {
        return stations.contains(station);
    }
}
