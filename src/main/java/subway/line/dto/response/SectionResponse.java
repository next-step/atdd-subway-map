package subway.line.dto.response;

public class SectionResponse {

    private final long lineId;

    private final long sectionId;

    private final int distance;

    private final long upStationId;

    private final long downStationId;

    public SectionResponse(long lineId, long sectionId, int distance, long upStationId, long downStationId) {
        this.lineId = lineId;
        this.sectionId = sectionId;
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

}
