package subway.line.domain;

import subway.station.domain.Station;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
public class Stations implements Serializable {

    @OneToOne
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStationId;

    @OneToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStationId;

    public Stations() {}

    public Stations(Station upStationId, Station downStationId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public Station getUpStationId() {
        return upStationId;
    }

    public Station getDownStationId() {
        return downStationId;
    }

    @Override
    public String toString() {
        return "Stations{" +
                "upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                '}';
    }
}
