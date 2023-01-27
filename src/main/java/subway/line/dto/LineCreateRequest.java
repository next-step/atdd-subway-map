package subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.entity.Line;
import subway.section.entity.Section;
import subway.station.entity.Station;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineCreateRequest {

    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private long distance;

    public Line toEntity(Station upStation, Station downStation) {
        Line line = Line.of(name, color);
        Section section = toSection(upStation, downStation);
        line.addSection(section);
        return line;
    }

    private Section toSection(Station upStation, Station downStation) {
        return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(this.distance)
                .build();
    }
}
