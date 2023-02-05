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
    @JoinColumn(name = "upStation_id")
    private Station upStation;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "downStation_id")
    private Station downStation;

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

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static void validateStations(Long upStationId, Long downStationId) {
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
