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
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    public Stations() {}

    public Stations(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    @Override
    public String toString() {
        return "Stations{" +
                "upStation=" + upStation +
                ", downStation=" + downStation +
                '}';
    }
}
