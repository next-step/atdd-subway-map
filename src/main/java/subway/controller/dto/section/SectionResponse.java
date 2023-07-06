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

    public static SectionResponse from(Section line) {
        return SectionResponse.builder()
                              .id(line.getId())
                              .upStation(StationResponse.from(line.getUpStation()))
                              .downStation(StationResponse.from(line.getDownStation()))
                              .distance(line.getDistance())
                              .build();
    }
}
