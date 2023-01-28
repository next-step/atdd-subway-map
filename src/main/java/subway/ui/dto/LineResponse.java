package subway.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineResponse {
    private long id;
    private String name;
    private String color;
    @JsonProperty("stations")
    private List<StationResponse> stationResponses;

    private LineResponse() {}

    public LineResponse(final long id, final String name, final String color, final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse createResponse(final Line saveLine) {
        return new LineResponse(saveLine.getId(), saveLine.getName(), saveLine.getColor(), convertToStationResponse(saveLine.getUpStation(), saveLine.getDownStation()));
    }

    private static List<StationResponse> convertToStationResponse(final Station upStation, final Station downStation) {
        return Stream.of(upStation, downStation)
                .map(StationResponse::of)
                .collect(Collectors.toList());
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
