package nextstep.subway.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    private Long upStationId;

    private Long downStationId;

    private Integer distance;

    protected Section() {
    }

    public Section(Line line, Long upStationId, Long downStationId, Integer distance) {
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
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

    public boolean containsStationId(Long stationId) {
        return Objects.equals(upStationId, stationId) || Objects.equals(downStationId, stationId);
    }
}
