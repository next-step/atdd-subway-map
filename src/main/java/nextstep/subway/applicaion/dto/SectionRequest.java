package nextstep.subway.applicaion.dto;

import javax.validation.constraints.NotBlank;

public class SectionRequest {
    @NotBlank(message = "상행역이 비어있습니다")
    private String upStationId;
    @NotBlank(message = "하행역이 비어있습니다.")
    private String downStationId;
    @NotBlank(message = "역 사이의 거리가 비어있습니다.")
    private String distance;

    public SectionRequest() {
    }

    public SectionRequest(String upStationId, String downStationId, String distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getDistance() {
        return distance;
    }
}
