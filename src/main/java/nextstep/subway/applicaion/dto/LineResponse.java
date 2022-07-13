package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.Setter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color,Long upStationId,
                        Long downStationId, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.stations = stations;
    }
    public static LineResponse of(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getUpStationId(),
                line.getDownStationId(),
                line.getStations()
                        .stream().map(StationResponse::of)
                        .collect(Collectors.toList())
        );
    }
}
