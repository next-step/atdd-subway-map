package subway.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SectionRequestTestDto {
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    @Builder
    public SectionRequestTestDto(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
