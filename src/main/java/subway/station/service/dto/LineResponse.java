package subway.station.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.line.Line;
import subway.station.domain.section.Section;
import subway.station.domain.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class LineResponse {

    protected Long id;
    protected String name;
    protected String color;
    protected List<Station> stations = new ArrayList<>();

    @Builder
    public LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        List<Station> stations = convertToStations(line.getSections());
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stations)
                .build();
    }

    public static List<LineResponse> from(List<Line> lines) {
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public static LineResponse fromForUpdate(Line line) {
        return LineResponse.builder()
                .name(line.getName())
                .color(line.getColor())
                .build();
    }

    private static List<Station> convertToStations(List<Section> orderedSections) {
        List<Station> stations = orderedSections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(orderedSections.get(orderedSections.size() - 1).getDownStation());
        return stations;
    }
}
