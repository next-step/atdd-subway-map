package subway.line.section;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SectionId implements Serializable {
    @Column
//    @Column(name = "lind_id")
    private Long lineId;
    @Column
//    @Column(name = "station_id")
    private Long stationId;

    public SectionId() {
    }

    public SectionId(Long lineId, Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionId sectionId = (SectionId) o;
        return Objects.equals(lineId, sectionId.lineId) && Objects.equals(stationId, sectionId.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, stationId);
    }
}
