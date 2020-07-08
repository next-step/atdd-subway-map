package nextstep.subway.line.domain;

import nextstep.subway.config.BaseEntity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    @Embedded
    private LineStations lineStations = new LineStations();

    public Line() {
    }

    public Line(String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.startTime = line.getStartTime();
        this.endTime = line.getEndTime();
        this.intervalTime = line.getIntervalTime();
        this.color = line.getColor();
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public List<LineStation> getLineStations() {
        return lineStations.getLineStationsInOrder();
    }

    public void addStation(Long preStationId, Long stationId, Integer distance, Integer duration) {
        final LineStation newLineStation = new LineStation(preStationId, stationId, distance, duration);
        lineStations.add(newLineStation);
    }

    public void removeStationByStationId(Long stationId) {
        lineStations.removeByStationId(stationId);
    }
}
