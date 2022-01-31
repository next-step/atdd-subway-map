package nextstep.subway.applicaion.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<StationResponse> stations;
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

    private LineResponse(Long id, String name, String color, Set<StationResponse> stations,
                         LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public static LineResponse createLineAddSectionResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                getStations(line.getSections()),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private static Set<StationResponse> getStations(List<Section> sections) {
        Set<StationResponse> stations = new LinkedHashSet<>();
        for (Section section : sections) {
            stations.add(StationResponse.createStationResponse(section.getUpStation()));
            stations.add(StationResponse.createStationResponse(section.getDownStation()));
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

    public Set<StationResponse> getStations() {
        return Collections.unmodifiableSet(stations);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
