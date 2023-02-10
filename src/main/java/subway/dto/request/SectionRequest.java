package subway.dto.request;

import lombok.Getter;

@Getter
public class SectionRequest {
    public SectionRequest(String upStationId, String downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private String upStationId;
    private String downStationId;
    private int distance;
}
