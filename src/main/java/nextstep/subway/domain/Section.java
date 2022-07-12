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
        validate(line, upStationId, downStationId, distance);
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validate(Line line, Long upStationId, Long downStationId, Integer distance) {
        if (line.getSections() == null) {
            return;
        }
        if (!Objects.equals(upStationId, line.getDownStationId())) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 한다.");
        }
        if (line.containsStationId(downStationId)) {
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
        }
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
