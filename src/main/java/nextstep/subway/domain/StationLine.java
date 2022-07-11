package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long distance;
    private Long upStationId;
    private Long downStationId;

    public StationLine() {
    }

    public StationLine(String name, String color, Long distance, Long upStationId, Long downStationId) {
        this.name = name;
        this.distance = distance;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public StationLine(Long id, String name, String color, Long distance, Long upStationId, Long downStationId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationLine that = (StationLine) o;
        return Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(distance, that.distance) && Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, distance, upStationId, downStationId);
    }
}
