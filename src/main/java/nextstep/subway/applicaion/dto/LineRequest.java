package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

@Getter
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

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
