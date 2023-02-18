package subway.line.domain;

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

    @Column(name = "up_station_id", nullable = false)
    private Long upStationId;

    @Column(name = "down_station_id", nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private long distance;

    public Line(String name, String color, Long upStationId, Long downStationId, long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line() {
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

    public long getDistance() {
        return distance;
    }

    public Line update(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }
}
