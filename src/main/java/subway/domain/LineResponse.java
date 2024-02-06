package subway.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public static LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(),
                                line.getName(),
                                line.getColor(),
                                StationResponse.createStationsResponse(line.getUpStation(), line.getDownStation()));
    }
}
