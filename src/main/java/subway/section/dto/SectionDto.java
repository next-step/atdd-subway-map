package subway.section.dto;

import java.util.List;

public class SectionDto {
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SectionDto(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionDto from(SectionRequest sectionRequest) {
        return new SectionDto(
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(),
                sectionRequest.getDistance()
        );
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public List<Long> getStationIds() { return List.of(upStationId, downStationId); }
}
