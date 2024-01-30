package subway.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color; // TODO: Enum 분리 고려

    private int upStationId;

    private int downStationId;

    private int distance;

    public StationLine() {
    }

    public StationLine(String name, String color, int upStationId, int downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationLine update(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
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

    public int getUpStationId() {
        return upStationId;
    }

    public int getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
