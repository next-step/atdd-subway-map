package nextstep.subway.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
public class Stations {

    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStations;

    @ManyToOne
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
