package subway.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final Long distance;
    private final List<StationResponse> stations;

    public static LineResponse createLineResponse(Line line) {
        List<Station> stations = new ArrayList<>();
        if (!line.getSections().isEmpty()) {
            stations.add(line.getSections().get(0).getUpStation());
        }

        stations.addAll(line.getSections().stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList()));

        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance(), stations.stream()
            .map(StationResponse::createStationResponse)
            .collect(Collectors.toList()));
    }

    protected LineResponse(Long id, String name, String color, Long distance, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
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

    public Long getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
