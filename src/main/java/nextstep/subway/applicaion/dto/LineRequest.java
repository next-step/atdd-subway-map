package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class LineRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @Min(1)
    private Long upStationId;
    @Min(1)
    private Long downStationId;
    @Min(0)
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
