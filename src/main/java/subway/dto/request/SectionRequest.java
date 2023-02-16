package subway.dto.request;

import lombok.Getter;

@Getter
public class SectionRequest {
    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private Long upStationId;
    private Long downStationId;
    private int distance;

    public static SectionRequest of(LineRequest request) {
        return new SectionRequest(request.getUpStationId(), request.getDownStationId(), request.getDistance());
    }
}
