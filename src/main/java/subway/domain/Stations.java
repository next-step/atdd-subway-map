package subway.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Embeddable
public class Stations {

    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;

    protected Stations() {
    }

    public Stations(final Station upStation, final Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public void updateDownStation(final Station station) {
        this.downStation = station;
    }

    public boolean equalDownStation(final Station station) {
        return this.downStation == station;
    }
}
