package subway.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    @JsonProperty("stations")
    private List<StationResponse> stationResponses;

    private LineResponse() {}

    public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse createResponse(final Line saveLine) {
        List<StationResponse> stationResponses = convertToStationResponse(saveLine.getSections().getSections());
        return new LineResponse(saveLine.getId(), saveLine.getName(), saveLine.getColor(), stationResponses);
    }

    private static List<StationResponse> convertToStationResponse(final List<Section> sections) {
        final List<Station> stations = convertToStation(sections);
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    private static List<Station> convertToStation(final List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public Long getId() {
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
