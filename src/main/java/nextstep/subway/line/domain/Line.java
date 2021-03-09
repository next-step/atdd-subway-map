package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Long upStationId;
    private Long downStationId;
    private String color;

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId == null ? downStationId : upStationId;
        this.downStationId = downStationId;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.upStationId = line.getUpStationId();
        this.downStationId = line.getDownStationId();
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

    public Long getUpStationId() { return upStationId; };

    public Long getDownStationId() { return downStationId; };
}
