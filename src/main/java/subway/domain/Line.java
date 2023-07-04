package subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity

public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "NAME")
    private String name;

    @Column(name = "UP_STATION_ID")
    private Long upStationId;

    @Column(name = "DOWN_STATION_ID")
    private Long downStationId;

    @Column(name = "DISTANCE")
    private int distance;

    public Line() {
    }


    public Line(String name, Long upStationId, Long downStationId) {
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public Line(Long id, String color, String name, Long upStationId, Long downStationId, int distance) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId = upStationId;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

}
