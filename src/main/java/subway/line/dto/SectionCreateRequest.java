package subway.line.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.line.entity.Line;
import subway.section.entity.Section;
import subway.station.entity.Station;

@Getter
@Setter
@NoArgsConstructor
public class SectionCreateRequest {

    private long lineId;
    private long downStationId;
    private long upStationId;
    private long distance;

    public Section toEntity(Line line, Station upStation, Station downStation) {
        return Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
