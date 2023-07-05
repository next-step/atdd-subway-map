package subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import subway.constants.LineConstant;
import subway.line.model.Line;
import subway.station.model.Station;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class LineCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    @NotBlank
    private Long upStationId;

    @NotBlank
    private Long downStationId;

    @NotBlank
    @Min(value = 1L, message = LineConstant.DISTANCE_MIN_MESSAGE)
    private Long distance;

    public static Line to(LineCreateRequest request,
                          Station upStation,
                          Station downStation) {
        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
    }
}
