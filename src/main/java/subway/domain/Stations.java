package subway.domain;

import java.util.List;
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

    public Stations(final List<Station> stations, final Long upStationId, final Long downStationId) {
        this.upStation = findStationById(stations, upStationId);
        this.downStation = findStationById(stations, downStationId);
    }

    private Station findStationById(final List<Station> stations, final Long stationId) {
        return stations.stream()
                .filter(station -> station.getId() == stationId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("노선 지하철 정보가 올바르지 않습니다."));
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
