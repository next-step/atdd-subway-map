package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineUpdateRequest;

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

    private long upStationId;

    private long downStationId;

    private long distance;

    public Line() {
    }

    public Line(String name, String color, long upStationId, long downStationId, long distance ) {
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

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public void update(LineUpdateRequest request) {
        this.name = request.getName();
        this.color = request.getColor();
    }
}
