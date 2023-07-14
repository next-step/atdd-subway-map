package subway.linesection;

import java.io.Serializable;

public class LineSectionPK implements Serializable {
    private Long lineId;
    private Long currentStationId;

    public static LineSectionPK of(Long lineId, Long stationId) {
        LineSectionPK lineSectionPK = new LineSectionPK();
        lineSectionPK.lineId = lineId;
        lineSectionPK.currentStationId = stationId;
        return lineSectionPK;
    }
}
