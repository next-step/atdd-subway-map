package subway.line;

import subway.station.Station;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToOne
    @JoinColumn(name = "upStationId")
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "downStationId")
    private Station downStation;

    private Long distance;

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
