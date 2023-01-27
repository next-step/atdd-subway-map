package subway.section.dto;

import lombok.*;
import subway.line.dto.LineResponse;
import subway.section.entity.Section;
import subway.station.dto.StationResponse;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {

    private Long sectionId;
    private LineResponse lineResponse;
    private StationResponse downStation;
    private StationResponse upStation;
    private long distance;

    public static SectionResponse from(Section section) {
        return SectionResponse.builder()
                .sectionId(section.getId())
                .lineResponse(LineResponse.from(section.getLine()))
                .downStation(StationResponse.from(section.getDownStation()))
                .upStation(StationResponse.from(section.getUpStation()))
                .distance(section.getDistance())
                .build();
    }
}
