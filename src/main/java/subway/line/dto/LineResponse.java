package subway.line.dto;

import lombok.Builder;
import subway.line.Line;
import subway.section.Section;
import subway.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    @Builder
    public LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        List<Station> upStations = line.getSections().stream().map(Section::getUpStation).collect(Collectors.toList());
        List<Station> downStations = line.getSections().stream().map(Section::getDownStation).collect(Collectors.toList());
        List<Station> stations = new ArrayList<>();
        stations.addAll(upStations);
        stations.addAll(downStations);

        return LineResponse.builder().id(line.getId()).name(line.getName()).color(line.getColor())
                .stations(stations)
                .build();
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

    public List<Station> getStations() {
        return stations;
    }
}
