package subway.section.domain;

import subway.station.domain.Station;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class Section {

    @ManyToOne
    private Station upStation;

    @ManyToOne
    private Station downStation;

    private Long distance;

    public Section() {
    }

    public Section(Station upStation, Station downStation, Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean hasStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
