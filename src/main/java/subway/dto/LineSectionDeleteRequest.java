package subway.dto;

public class LineSectionDeleteRequest {
    private Long stationId;

    public LineSectionDeleteRequest() {
    }

    public LineSectionDeleteRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
