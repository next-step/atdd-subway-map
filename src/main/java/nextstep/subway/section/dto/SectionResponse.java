package nextstep.subway.section.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.List;

public class SectionResponse {

    private Long id;
    private String name;
    private String color;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<Station> stations;

    public SectionResponse() {
    }

    public SectionResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.stations = stations;
    }

    public static SectionResponse of(Line line) {
        return new SectionResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate(),
                line.getStations());
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

    public List<Station> getStations() {
        return stations;
    }
}
