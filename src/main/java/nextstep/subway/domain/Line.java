package nextstep.subway.domain;

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

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Line update(Line line) {
        if (line.getName() != null) {
            name = line.getName();
        }
        if (line.getColor() != null) {
            color = line.getColor();
        }
        if (line.getUpStationId() != null) {
            upStationId = line.getUpStationId();
        }
        if (line.getDownStationId() != null) {
            downStationId = line.getDownStationId();
        }
        if (line.getDistance() != null) {
            distance = line.getDistance();
        }
        return this;
    }
}
