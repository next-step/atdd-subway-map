package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Section;

@Getter
@Builder
public class SectionResponse {

    private Long id;
    private Integer distance;
    private StationResponse upStation;
    private StationResponse downStation;

    public static SectionResponse from(Section section) {
        return SectionResponse.builder()
                .id(section.id())
                .distance(section.distance().value())
                .upStation(StationResponse.from(section.upStation()))
                .downStation(StationResponse.from(section.downStation()))
                .build();
    }

}
