package nextstep.subway.domain;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class Stations {

    @ManyToOne(fetch = LAZY)
    private Station upStations;

    @ManyToOne(fetch = LAZY)
    private Station downStation;

    public Stations() {
    }

    public Stations(Station upStations, Station downStation) {
        this.upStations = upStations;
        this.downStation = downStation;
    }

    public Station getUpStations() {
        return upStations;
    }

    public Station getDownStation() {
        return downStation;
    }
}
