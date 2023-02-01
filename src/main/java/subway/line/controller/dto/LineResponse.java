package subway.line.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;
import subway.station.controller.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    @Builder(access = AccessLevel.PROTECTED)
    private LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations.stream()
                .map(StationResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public static LineResponse fromDomain(Line line) {
        return LineResponse
                .builder().id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(
                        List.of(line.getUpStation(), line.getDownStation())
                ).build();
    }

}