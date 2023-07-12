package subway.linesection;

import javax.persistence.*;

@Entity
@IdClass(LineSectionPK.class)
public class LineSection {
    @Id
    private Long lineId;
    @Column(nullable = true)
    private Long previousStationId;
    @Id
    private Long currentStationId;
    @Column(nullable = true)
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

    public void lineNextLineSection(LineSection nextLineSection) {
        this.nextStationId = nextLineSection.getCurrentStationId();
        nextLineSection.previousStationId = this.currentStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getCurrentStationId() {
        return currentStationId;
    }

}
