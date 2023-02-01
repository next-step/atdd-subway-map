package subway.domain;

import subway.exception.StationNotFoundException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
        validateStations(upStationId, downStationId);

        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validateStations(Long upStationId, Long downStationId) {
        if (upStationId == null || downStationId == null) {
            throw new StationNotFoundException();
        }
    }

    public void update(String name, String color) {
        this.name = name;
        this.color =color;
    }
}
