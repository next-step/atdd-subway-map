package nextstep.subway.section.application.dto;

import nextstep.subway.section.application.manager.LineData;
import nextstep.subway.section.application.manager.StationData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SectionLineResponse {
    private Long id;
    private String name;
    private String color;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private List<StationData> stations;

    public SectionLineResponse() {
    }

    public SectionLineResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate, List<StationData> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.stations = stations;
    }

    public List<StationData> getStations() {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public static SectionLineResponse of(LineData line, List<StationData> stations) {
        return new SectionLineResponse(line.getId(), line.getName(), line.getColor(), line.getCreatedDate(), line.getModifiedDate(), stations);
    }
}
