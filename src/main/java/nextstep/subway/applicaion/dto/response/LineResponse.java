package nextstep.subway.applicaion.dto.response;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private LineResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        stations = new ArrayList<>();
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
