package subway.line.domain;

import subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;
    private Integer distance;

    protected Line() {}

    public Line(String name, String color, Integer distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public void changeStations(Long upStationId, Long downStationId){
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public void changeLine(String name, String color){
        this.name = name;
        this.color = color;
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

    public Integer getDistance() {
        return distance;
    }
}
