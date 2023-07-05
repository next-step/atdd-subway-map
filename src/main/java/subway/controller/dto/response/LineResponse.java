package subway.controller.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        StationResponse upStation = StationResponse.from(line.getUpStation());
        StationResponse downStation = StationResponse.from(line.getDownStation());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation));
    }
}
