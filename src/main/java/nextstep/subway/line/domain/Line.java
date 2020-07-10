package nextstep.subway.line.domain;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.config.BaseEntity;
import nextstep.subway.exception.NotRegisteredException;

@Entity
@NoArgsConstructor
@Getter
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

    public LineStation addLineStation(LineStation lineStation) {
        return lineStations.add(lineStation);
    }

    public LineStation unregisterLineStation(LineStation lineStation) {
        adjustPreStationIdOfPriorToUnregisteredLineStation(lineStation);
        try {
            lineStations.remove(lineStation);
        } catch (Exception e) {
            throw new NotRegisteredException("this station is not registered to the given line id.");
        }
        return lineStation;
    }

    public LineStation findLineStationByLineStationId(Long lineStationId) {
        return lineStations.findLineStationByLineStationId(lineStationId);
    }

    private void adjustPreStationIdOfPriorToUnregisteredLineStation(LineStation lineStation) {
        lineStations.adjustPreStationIdOfPriorToUnregisteredLineStation(lineStation);
    }
}
