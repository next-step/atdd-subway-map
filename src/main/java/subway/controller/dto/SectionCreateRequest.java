package subway.controller.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
public class SectionCreateRequest {

    @NotBlank
    private String downStationId;
    @NotBlank
    private String upStationId;
    private long distance;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(String downStationId, String upStationId, long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public List<Long> stationIds(){
        return List.of(Long.parseLong(upStationId), Long.parseLong(downStationId));
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public long getDistance() {
        return distance;
    }
}
