package subway.section.dto;

import lombok.*;
import subway.section.entity.Section;
import subway.station.dto.StationResponse;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {

    private Long id;
    private StationResponse downStation;
    private StationResponse upStation;
    private long distance;

    public static SectionResponse from(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .downStation(StationResponse.from(section.getDownStation()))
                .upStation(StationResponse.from(section.getUpStation()))
                .distance(section.getDistance())
                .build();
    }
}
