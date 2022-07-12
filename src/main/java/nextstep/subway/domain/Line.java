package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    public Line() {
    }

    public Line(Long id, String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line(LineRequest lineRequest) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.upStationId = lineRequest.getUpStationId();
        this.downStationId = lineRequest.getDownStationId();
        this.distance = lineRequest.getDistance();
    }

    public Line update(LineRequest lineRequest) {
        return new Line(
                this.id,
                lineRequest.getName().isEmpty() ? this.name : lineRequest.getName(),
                lineRequest.getColor().isEmpty() ? this.color : lineRequest.getColor(),
                lineRequest.getUpStationId() == null ? this.upStationId : lineRequest.getUpStationId(),
                lineRequest.getDownStationId() == null ? this.downStationId : lineRequest.getDownStationId(),
                lineRequest.getDistance() == null ? this.distance : lineRequest.getDistance()
        );
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
