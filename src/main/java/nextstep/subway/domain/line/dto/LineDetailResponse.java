package nextstep.subway.domain.line.dto;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineDetailResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    private LineDetailResponse(Long id, String name, String color, List<Station> stations,
                               LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.stations = initStations(stations);
    }

    public static LineDetailResponse from(Line line) {
        return new LineDetailResponse(line.getId(), line.getName(), line.getColor(), line.getStationList(),
                line.getCreatedDate(), line.getModifiedDate());
    }

    private List<StationResponse> initStations(List<Station> stations) {
        if (stations != null) {
            return stations.stream().map(StationResponse::from).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
