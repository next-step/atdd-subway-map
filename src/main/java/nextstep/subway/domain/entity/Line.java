package nextstep.subway.domain.entity;

import nextstep.subway.domain.service.LineValidator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public Line() {
    }

    public Line(final String name,
                final String color,
                final Long upStationId,
                final Long downStationId,
                final int distance,
                final LineValidator lineValidator) {
        lineValidator.validateLine(name, color);

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

    public int getDistance() {
        return distance;
    }

    public void change(final String name, final String color, final LineValidator lineValidator) {
        lineValidator.validateLine(name, color);

        this.name = name;
        this.color = color;
    }
}
