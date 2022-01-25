package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LineDetailResponse {

    public static LineDetailResponse from(Line line) {
        return new LineDetailResponse(line.getId(), line.getName(), line.getColor(), line.getStations(),
                line.getCreatedDate(), line.getModifiedDate());
    }

    private final Long id;
    private final String name;
    private final String color;
    private final List<?> stations;
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

    public List<?> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    private LineDetailResponse(Long id, String name, String color, List<?> stations,
                               LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.stations = initStations(stations);
    }

    private List<?> initStations(List<?> stations) {
        if (stations != null) {
            return stations;
        }
        return new ArrayList<>();
    }
}
