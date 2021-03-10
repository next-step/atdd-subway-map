package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.common.exception.ApplicationType;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private String color;

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId == null ? downStationId : upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.upStationId = line.getUpStationId();
        this.downStationId = line.getDownStationId();
    }

    public void extendsLine(Long downStationId, int distance) {
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public boolean validateExtentionAvailable(Long newUpstationId, Long newDownStationId, List<Section> sections) {
        if (this.downStationId != newUpstationId) {
            return false;
        }

        Long registerdStationCount = sections.stream().filter(section -> section.getUpStationId() == newDownStationId || section.getDownStationId() == newDownStationId).count();
        return registerdStationCount <= 0;
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

    public int getDistance() { return distance; }
}
