package subway.model;

import subway.exception.CreateLineSectionException;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Stations {
    @OneToMany
    @JoinColumn(name = "line_id")
    private List<Station> stations;

    private Long upStationId;

    private Long downStationId;

    protected Stations() {
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
        this.upStationId = stations.get(0).getId();
        this.downStationId = stations.get(stations.size() - 1).getId();
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public void createLineSection(Station upStation, Station downStation) {
        if (!isEquals(upStation)) {
            throw new CreateLineSectionException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
        }
        if (isContains(downStation)) {
            throw new CreateLineSectionException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.");
        }
        this.stations = addStation(downStation);
        this.downStationId = downStation.getId();
    }

    private List<Station> addStation(Station downStation) {
        List<Station> stationList = new ArrayList<>(this.stations);
        stationList.add(downStation);
        return stationList;
    }

    private boolean isContains(Station station) {
        return this.stations.contains(station);
    }

    private boolean isEquals(Station station) {
        return Objects.equals(this.downStationId, station.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stations stations1 = (Stations) o;
        return Objects.equals(stations, stations1.stations) && Objects.equals(upStationId, stations1.upStationId) && Objects.equals(downStationId, stations1.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, upStationId, downStationId);
    }
}
