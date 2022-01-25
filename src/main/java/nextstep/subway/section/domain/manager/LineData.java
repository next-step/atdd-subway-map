package nextstep.subway.section.domain.manager;

import nextstep.subway.line.domain.Line;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LineData {
    private Long id;
    private String name;
    private String color;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private List<StationData> stations;
    public LineData() {
    }

    public LineData(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.stations = new ArrayList<>();
    }

    public void setStations(List<StationData> stations) {
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

    public static LineData of(Line line) {
        return new LineData(line.getId(), line.getName(), line.getColor(), line.getCreatedDate(), line.getModifiedDate());
    }
}
