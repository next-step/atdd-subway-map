package subway.section.dto;

import lombok.*;
import subway.section.entity.Section;
import subway.station.entity.Station;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateRequest {

    private long lineId;
    private long downStationId;
    private long upStationId;
    private long distance;

    public Section toEntity(Station upStation, Station downStation) {
        return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
