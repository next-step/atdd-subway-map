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
    private Integer distance;
    public Line() {
    }

    public Line(String name, String color) {
        this(name, color, 0L, 0L);
    }

    public Line(String name, String color, Long upStationId, Long downStationId) {
        this(0L, name, color, upStationId, downStationId);
    }

    public Line(Long id, String name, String color, Long upStationId, Long downStationId) {
        this(id, name, color, upStationId, downStationId, 0);
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this(0L, name, color, upStationId, downStationId, distance);
    }

    public Line(Long id, String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.id = id;
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

    public Integer getDistance() {
        return distance;
    }
}
