package nextstep.subway.domain.subwayLine;

import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.subwayLineColor.SubwayLineColor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "subway_line_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "distance")
    private Integer distance;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "color_id")
    private SubwayLineColor color;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @OneToMany(mappedBy = "subwayLine", fetch = LAZY)
    private List<Station> stations = new ArrayList<>();

    public SubwayLine() {
    }

    public SubwayLine(String name, Integer distance, SubwayLineColor color, Station upStation, Station downStation) {
        this.name = name;
        this.distance = distance;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    @PrePersist
    void prePersist() {
        this.upStation.updateSubwayLine(this);
        this.downStation.updateSubwayLine(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SubwayLineColor getColor() {
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

    public List<Station> getStations() {
        return stations;
    }

    public void updateStations(List<Station> stations) {
        this.stations.addAll(stations);
    }
}
