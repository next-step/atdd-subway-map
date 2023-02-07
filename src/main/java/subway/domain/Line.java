package subway.domain;

import subway.exception.StationNotFoundException;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "downStation_id")
    private Station downStation;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "upStation_id")
    private Station upStation;

    private Long distance;

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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    public Line(String name, String color, Station downStation, Station upStation, Long distance) {
        this.name = name;
        this.color = color;
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
    }

    public static void validateStations(Long downStationId, Long upStationId) {
        if (upStationId == null || downStationId == null) {
            throw new StationNotFoundException();
        }
    }

    public void update(String name, String color) {
        this.name = name;
        this.color =color;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }
}
