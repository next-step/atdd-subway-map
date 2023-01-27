package subway.line.dto;

import lombok.*;
import subway.line.entity.Line;
import subway.station.dto.StationResponse;
import subway.station.entity.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineResponse {
    private long id;
    private String name;
    private String color;
    private List<StationResponse> stationResponses = new ArrayList<>();

    public static LineResponse from(Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .stationResponses(createSectionResponses(line))
                .color(line.getColor())
                .build();
    }

    private static List<StationResponse> createSectionResponses(Line line) {
        return line.stations()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}


