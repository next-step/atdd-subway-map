package subway.line;

import subway.station.Station;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(name = "upStationId")
    private Station upStationId;

    @ManyToOne
    @JoinColumn(name = "downStationId")
    private Station downStationId;

    @Column(nullable = false)
    private Long distance;

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStation;
        this.downStationId = downStation;
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

    public Station getUpStationId() {
        return upStationId;
    }

    public Station getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
