package subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import subway.line.model.Line;
import subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LineResponse {
    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static LineResponse from(Line line) {
        List<StationResponse> stationResponses = line.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponses)
                .build();
    }
}
