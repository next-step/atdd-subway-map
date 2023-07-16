package subway.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long distance;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    public Line() {

    }

    public Line(LineRequest lineRequest, Long upStationId, Long downStationId) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.distance = lineRequest.getDistance();
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public void updateLine(LineRequest lineRequest) {
        this.name = lineRequest.getName().isBlank() ? this.name : lineRequest.getName();
        this.color = lineRequest.getColor().isBlank() ? this.color : lineRequest.getColor();
        this.distance = lineRequest.getDistance() == null ? this.distance : lineRequest.getDistance();
    }

    public void updateLineStation(Section section) {
        this.downStationId = section.getDownStationId();
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
