package nextstep.subway.applicaion.dto.response;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Set<Station> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private LineResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = new LinkedHashSet<>();
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private LineResponse(Long id, String name, String color, Set<Station> stations, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse createLineAddSectionResponse(Line line, List<Section> sections) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                getStations(sections),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private static Set<Station> getStations(List<Section> sections) {
        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
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

    public Set<Station> getStations() {
        return Collections.unmodifiableSet(stations);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
