package subway.line.dto.request;

public class SectionRequest {

    private final long downStationId;
    private final long upStationId;
    private final long distance;

    public SectionRequest(long downStationId, long upStationId, long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }
}
