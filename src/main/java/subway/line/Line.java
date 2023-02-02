package subway.line;

import subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    @Column(nullable = false)
    private Long distance;

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        addStation(upStation);
        addStation(downStation);
    }

    protected Line() {
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

    public void addStation(Station station) {
        sections.add(new Section(station, this));
    }

    public Long getDistance() {
        return distance;
    }

    public List<Station> getStationList() {
        return this.sections.stream().map(Section::getStation).collect(Collectors.toList());
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
