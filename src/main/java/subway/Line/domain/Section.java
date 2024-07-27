package subway.Line.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    public Section() {}

    public Section(Long upStationId, Long downStationId, int distance, Line line) {
        this(null, upStationId, downStationId, distance, line);
    }

    public Section(Long id, Long upStationId, Long downStationId, int distance, Line line) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.line = line;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public boolean isDownStationId(Long stationId) {
        return downStationId.equals(stationId);
    }

    public void remove() {
        this.line = null;
    }

    public int getDistance() {
        return distance;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                ", line=" + line +
                '}';
    }
}
