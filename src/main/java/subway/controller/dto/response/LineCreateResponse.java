package subway.controller.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineCreateResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineCreateResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineCreateResponse from(Line line) {
        StationResponse upStation = StationResponse.from(line.getUpStation());
        StationResponse downStation = StationResponse.from(line.getDownStation());

        return new LineCreateResponse(line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation));
    }
}
