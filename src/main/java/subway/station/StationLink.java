package subway.station;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Embeddable
public class StationLink {
    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;

    public StationLink() {
    }

    public StationLink(Station upStation,
                       Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
