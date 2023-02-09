package subway.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Stations {
    @OneToMany
    @JoinColumn(name = "line_id")
    private List<Station> stations;

    private Long upStationId;

    private Long downStationId;

    public Stations() {
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
