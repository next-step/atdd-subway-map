package subway.subwayline;

import subway.station.Station;

import javax.persistence.*;

@Entity
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStationId;

    private Integer distance;

    protected SubwayLine() {}

    public SubwayLine(String name, String color, Station upStationId, Station downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SubwayLine of(String name, String color, Station upStationId, Station downStationId, Integer distance) {
        return new SubwayLine(name, color, upStationId, downStationId, distance);
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

    public Integer getDistance() {
        return distance;
    }

    public void modifySubwayLine(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
