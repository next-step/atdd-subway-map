package subway.application.dto;

public class SectionDeleteDto {
    private Long lineId;
    private Long stationId;
    public SectionDeleteDto(Long lineId, Long stationId) {
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
