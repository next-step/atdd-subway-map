package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import subway.domain.Line;

@Getter
@Builder
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse of(Line line) {

        List<StationResponse> stations = line.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return LineResponse.builder()
            .id(line.getId())
            .name(line.getName())
            .color(line.getColor())
            .stations(stations)
            .build();
    }

}
