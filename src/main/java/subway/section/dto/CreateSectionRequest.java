package subway.section.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateSectionRequest {

    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionDto toDto() {
        return SectionDto.builder()
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
