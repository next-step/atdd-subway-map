package nextstep.subway.line.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    @JsonIgnore
    private Long lastSectionId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private LineResponse() {
    }

    private LineResponse(Long id, String name, String color, List<StationResponse> stations, Long lastSectionId, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.lastSectionId = lastSectionId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse of(Line line) {
        List<StationResponse> stationResponses = line.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                stationResponses,
                line.getLastSectionId(),
                line.getCreatedDate(),
                line.getModifiedDate());
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getLastSectionId() {
        return lastSectionId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
