package nextstep.subway.domain.line;

import nextstep.subway.domain.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
@Embeddable
public class Stations {
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
//    @JoinColumn(name = "id")
    private List<Station> stations = new ArrayList<>();

    protected Stations() {/*no-op*/}

    public Stations(final List<Station> stations) {
        if (stations.size() < 2) {
            throw new IllegalArgumentException("역은 상행 종점역, 하행 종점역 최소 두개의 정보를 포함해야 합니다.");
        }

        this.stations = new ArrayList<>(stations);
    }

    public List<Station> getStations() {
        return List.copyOf(stations);
    }
}
