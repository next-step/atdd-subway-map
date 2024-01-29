package subway.line;

public class SectionsUpdateRequest {
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    public SectionsUpdateRequest(Long downStationId,
                                 Long upStationId,
                                 Long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }
}
