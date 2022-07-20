package nextstep.subway.applicaion.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SectionRequest {
    @NotBlank(message = "상행역이 비어있습니다")
    private Long upStationId;
    @NotBlank(message = "하행역이 비어있습니다.")
    private Long downStationId;
    @NotBlank(message = "역 사이의 거리가 비어있습니다.")
    private Integer distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
