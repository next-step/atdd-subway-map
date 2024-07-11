package subway.section;

public class SectionDeleteDTO {

    private Long lineId;
    private Long stationId;

    public SectionDeleteDTO(Long lineId, Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }
}
