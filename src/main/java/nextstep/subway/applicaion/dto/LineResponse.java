package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Sections;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<SectionResponse> sections;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;
    private final List<StationResponse> stations;

    private LineResponse(Long id, String name, String color, List<SectionResponse> sections,
                         List<StationResponse> stations, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), sectionsResponseMapper(line.getSections()),
                stationResponseMapper(line.getSections()), line.getCreatedDate(), line.getModifiedDate());
    }

    private static List<SectionResponse> sectionsResponseMapper(Sections sections) {
        return sections.getSections().stream()
                .map(section -> new SectionResponse(section))
                .collect(Collectors.toList());
    }

    private static List<StationResponse> stationResponseMapper(Sections sections) {
        List<StationResponse> stationResponses = new ArrayList<>();
        sections.getSections().stream()
                .forEach(section -> {
                    stationResponses.add(new StationResponse(section.getDownStation()));
                    stationResponses.add(new StationResponse(section.getUpStation()));
                });
        List<StationResponse> result = stationResponses.stream().distinct().collect(Collectors.toList());
        return result;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
