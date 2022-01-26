package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Set<StationResponse> stations = new HashSet<>();
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public LineResponse(Long id, String name, String color, Set<StationResponse> sections, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = sections;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse of(Line line) {
        List<Section> sections = line.getSections();
        Set<StationResponse> result = new LinkedHashSet<>();
        for (Section section : sections) {
            result.add(StationResponse.of(section.getUpStation()));
            result.add(StationResponse.of(section.getDownStation()));
        }
        return new LineResponse(line.getId(), line.getName(), line.getColor(), result, line.getCreatedDate(), line.getModifiedDate());
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

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public Set<StationResponse> getStations() {
        return stations;
    }
}
