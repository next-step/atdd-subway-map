package subway.util;

import subway.dto.LineResponse;
import subway.dto.SectionResponse;
import subway.dto.StationDto;
import subway.dto.StationResponse;
import subway.model.Line;
import subway.model.Section;
import subway.model.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mapper {
    private Mapper() {
    }

    public static StationResponse toResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static LineResponse toResponse(Line line) {
        List<StationDto> stations = toStationDtoList(line.getSections());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations, line.getDistance());
    }

    private static List<StationDto> toStationDtoList(List<Section> sections) {
        Station firstStation = sections.get(0).getUpStation();
        Station lastStation = sections.get(sections.size() - 1).getDownStation();
        return Stream.of(firstStation, lastStation)
                .map(Mapper::toStationDto)
                .collect(Collectors.toList());
    }

    private static StationDto toStationDto(Station station) {
        return new StationDto(station.getId(), station.getName());
    }

    public static SectionResponse toResponse(Section section) {
        return new SectionResponse(section.getId(), section.getUpStation(), section.getDownStation(), section.getDistance());
    }
}
