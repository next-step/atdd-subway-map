package nextstep.subway.domain;

import nextstep.subway.exception.DeleteStationException;
import nextstep.subway.exception.StationNotRegisteredException;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    // FIXME 역간 거리를 관리하도록 List 로 변경
    private int distance;

    @OneToMany
    @JoinColumn(name = "line_id")
    private List<Station> stations;

    protected Line() {
    }

    public Line(String name, String color, List<Station> stations, int distance) {
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public Line changeBy(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
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

    public Line addSection(Station newStation, int distance) {
        stations.add(newStation);
        this.distance = distance;
        return this;
    }

    public Station lastStation() {
        return stations.stream().max(Comparator.comparing(Station::getId))
                .orElseThrow(() -> new StationNotRegisteredException("노선에 등록된 역이 없습니다."));
    }

    public boolean hasStation(long id) {
        return stations.stream().anyMatch(station -> station.equalsId(id));
    }

    public void deleteStation(Long stationId) {
        validateDeleteStation(stationId);
        stations = stations.stream()
                .filter(station -> !station.equalsId(stationId))
                .collect(Collectors.toList());
    }

    private void validateDeleteStation(Long stationId) {
        if (stations.size() == 1) {
            throw new DeleteStationException("상행역과 하행역만 존재하기 때문에 삭제할 수 없습니다.");
        }
        if (!lastStationId().equals(stationId)) {
            throw new DeleteStationException("하행역만 삭제할 수 있습니다.");
        }
    }

    private Long lastStationId() {
        return lastStation().getId();
    }
}
