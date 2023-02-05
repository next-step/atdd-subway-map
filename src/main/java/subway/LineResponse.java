package subway;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final Integer distance;
    private final List<StationResponse> stations;


    public LineResponse(Long id, String name, String color, Integer distance,
        List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }

    public static LineResponse createSubwayLineResponse(Line line) {

        return new LineResponse(line.getId(), line.getName(),
            line.getColor(), line.getDistance(),
            Stream.of(line.getUpStation(), line.getDownStation())
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList()));
    }
}
