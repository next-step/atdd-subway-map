package subway.linesection;

import javax.persistence.*;

@Entity
@IdClass(LineSectionPK.class)
public class LineSection {
    @Id
    private Long lineId;
    private Long previousStationId;
    @Id
    private Long currentStationId;
    private Long nextStationId;
    @Column(nullable = false)
    private Integer distance;

    public static LineSection ofFirst(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        LineSection lineSection = new LineSection();
        lineSection.lineId = lineId;
        lineSection.currentStationId = upStationId;
        lineSection.nextStationId = downStationId;
        lineSection.distance = distance;
        return lineSection;
    }

    public static LineSection of(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        LineSection lineSection = new LineSection();
        lineSection.lineId = lineId;
        lineSection.previousStationId = upStationId;
        lineSection.currentStationId = downStationId;
        lineSection.distance = distance;
        return lineSection;
    }

    public void linkSection(LineSection nextLineSection) {
        this.nextStationId = nextLineSection.getCurrentStationId();
        nextLineSection.previousStationId = this.currentStationId;
    }

    public void updateToLastSection() {
        this.nextStationId = null;
    }

    public boolean isStartSection() {
        return this.previousStationId == null;
    }

    public boolean isEndSection() {
        return this.nextStationId == null;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getCurrentStationId() {
        return currentStationId;
    }

    public Long getPreviousStationId() {
        return previousStationId;
    }

}
