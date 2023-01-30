package subway.stationline;

import subway.stationline.dto.StationLineInterface;

import javax.persistence.*;

@Entity
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Column(nullable = false)
    private Long upStationId;
    @Column(nullable = false)
    private Long downStationId;
    @Column(nullable = false)
    private Long distance;

    public StationLine() {
    }

    public StationLine(StationLineInterface stationLine) {
        this.name = stationLine.name();
        this.color = stationLine.color();
        this.upStationId = stationLine.upStationId();
        this.downStationId = stationLine.downStationId();
        this.distance = stationLine.distance();
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

}
