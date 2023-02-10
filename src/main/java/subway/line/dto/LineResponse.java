package subway.line.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;
import subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    @Builder(access = AccessLevel.PROTECTED)
    private LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse fromDomain(Line line) {
        var stationResponses = line.getSections().getStationList()
                .stream()
                .map(StationResponse::fromDomain)
                .collect(Collectors.toList());

        return LineResponse
                .builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponses)
                .build();
    }

}