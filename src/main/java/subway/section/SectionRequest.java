package subway.section;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.line.Line;
import subway.station.Station;

@Getter
@AllArgsConstructor
public class SectionRequest {

    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public Section toEntity(Line line, Station upStation, Station downStation) {
        return new Section(line, upStation, downStation, distance);
    }
}
