package subway;

import javax.persistence.*;

@Embeddable
public class StationLink {
    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;

    public StationLink() {
    }

    public StationLink(Long upStationId, Long downStationId) {
        this.upStation = new Station(upStationId);
        this.downStation = new Station(downStationId);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
