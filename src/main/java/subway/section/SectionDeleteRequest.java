package subway.section;

public class SectionDeleteRequest {
    private final Long stationId;

    public SectionDeleteRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
