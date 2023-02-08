package subway.controller.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SectionRequest {

    @NotBlank
    private Long upStationId;

    @NotBlank
    private Long downStationId;

    @NotBlank
    private Integer distance;

    @Builder
    private SectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

}
