package subway.line;

import com.fasterxml.jackson.annotation.JsonProperty;
import subway.station.StationResponse;

import java.util.List;

public class LineResponse {

    private long id;

    private String name;

    private String color;

    @JsonProperty("stations")
    private List<StationResponse> stationResponses;

    public LineResponse(long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stations;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(
                        StationResponse.of(line.getUpStation()),
                        StationResponse.of(line.getDownStation())
                )
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
