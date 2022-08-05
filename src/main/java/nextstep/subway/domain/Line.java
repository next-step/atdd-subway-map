package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "line")
public class Line {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "color")
    private String color;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @OneToMany(mappedBy = "line", fetch = LAZY)
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, Integer distance, String color, Station upStation, Station downStation) {
        this.name = name;
        this.distance = distance;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Line(Line subwayLine) {
        this.id = subwayLine.getId();
        this.name = subwayLine.getName();
        this.distance = subwayLine.getDistance();
        this.color = subwayLine.getColor();
        this.upStation = subwayLine.getUpStation();
        this.downStation = subwayLine.getDownStation();
        this.sections = subwayLine.getSections();
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

    public Integer getDistance() {
        return distance;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getAllStations() {
        final List<Station> stations = this.sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        stations.add(this.downStation);

        return stations;
    }

    public void performDelete(Section section) {
        this.upStation.removeLine();
        this.downStation.removeLine();
        this.sections.remove(section);
        this.upStation = null;
        this.downStation = null;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void updateDownStation(Station downStation) {
        this.downStation = downStation;
    }
}
