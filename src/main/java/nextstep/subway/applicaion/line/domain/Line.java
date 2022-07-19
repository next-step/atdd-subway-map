package nextstep.subway.applicaion.line.domain;

import nextstep.subway.applicaion.line.dto.LineRequest;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@DynamicUpdate
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long distance;
    private Long upStationId;
    private Long downStationId;

    public Line() {

    }

    public Line(LineRequest lineRequest) {
        this.id = lineRequest.getId();
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.distance = lineRequest.getDistance();
        this.upStationId = lineRequest.getUpStationId();
        this.downStationId = lineRequest.getDownStationId();
    }

    public void update(String name, String color) {
        this.color = color;
        this.name = name;
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

    public Long getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
