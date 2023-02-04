package subway;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class SubwayLineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final Integer distance;
    private final List<StationResponse> stations;


    public SubwayLineResponse(Long id, String name, String color, Integer distance,
        List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }

    public static SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine) {

        return new SubwayLineResponse(subwayLine.getId(), subwayLine.getName(),
            subwayLine.getColor(), subwayLine.getDistance(),
            Stream.of(subwayLine.getUpStation(), subwayLine.getDownStation())
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList()));
    }
}
