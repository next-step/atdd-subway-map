package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Section;

@Getter
@NoArgsConstructor
public class SectionResponse {
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    @Builder
    public SectionResponse(Long id, Long upStationId, Long downStationId, Integer distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return SectionResponse.builder()
            .id(section.getId())
            .upStationId(section.getUpStation().getId())
            .downStationId(section.getDownStation().getId())
            .distance(section.getDistance())
            .build();
    }

}
