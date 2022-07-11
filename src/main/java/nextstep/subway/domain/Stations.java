package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Stations {
    private static final int MINIMUM_STATIONS_SIZE = 2;
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Station> stations;

    protected Stations() {/*no-op*/}

    public Stations(final List<Station> stations) {
        if (stations.size() < MINIMUM_STATIONS_SIZE) {
            throw new IllegalArgumentException("역은 상행 종점역, 하행 종점역 최소 두개의 정보를 포함해야 합니다.");
        }

        this.stations = new ArrayList<>(stations);
    }

    public List<Station> getStations() {
        return List.copyOf(stations);
    }
}
