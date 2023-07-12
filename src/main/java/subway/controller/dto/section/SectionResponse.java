package subway.controller.dto.section;

import lombok.Builder;
import lombok.Getter;
import subway.controller.dto.station.StationResponse;
import subway.model.section.Section;

@Getter
@Builder
public class SectionResponse {

    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private Long distance;

    public static SectionResponse from(Section section) {
        return SectionResponse.builder()
                              .id(section.getId())
                              .upStation(StationResponse.from(section.getUpStation()))
                              .downStation(StationResponse.from(section.getDownStation()))
                              .distance(section.getDistance())
                              .build();
    }
}
