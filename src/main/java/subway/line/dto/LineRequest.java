package subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.entity.Line;
import subway.station.entity.Station;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineRequest {

    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private long distance;

    public Line toEntity(Station upStation, Station downStation) {
        return Line.builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
