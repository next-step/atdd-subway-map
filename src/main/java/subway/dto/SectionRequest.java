package subway.dto;

import lombok.Getter;

@Getter
public class SectionRequest {
    private String upStationId;
    private String downStationId;
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(String upStationId, String downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
