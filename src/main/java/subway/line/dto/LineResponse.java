package subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.dto.StationResponse;
import subway.line.entity.Line;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineResponse {
    private long id;
    private String name;
    private String color;
    private List<StationResponse> stations = new ArrayList<>();

    public static LineResponse from(Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponseList(line))
                .build();
    }

    public LineResponse(Line line) {
        this(line.getId(), line.getName(), line.getColor(), stationResponseList(line));
    }

    private static List<StationResponse> stationResponseList(Line line) {
        return List.of(
                StationResponse.from(line.getUpStation()),
                StationResponse.from(line.getDownStation()));
    }
}


