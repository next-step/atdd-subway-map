package subway.line.dto.response;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;
import subway.station.dto.response.StationResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    @Builder
    private LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        List<StationResponse> stations = new ArrayList<>();
        stations.add(StationResponse.from(line.getUpStation()));
        stations.add(StationResponse.from(line.getDownStation()));

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stations)
                .build();
    }
}
