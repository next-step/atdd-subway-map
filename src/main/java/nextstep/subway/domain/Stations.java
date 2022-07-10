package nextstep.subway.domain;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Stations {
    @ManyToOne
    public Station upStation;

    @ManyToOne
    public Station downStation;

    protected Stations() {

    }

    public List<Station> getValues() {
        return List.of(upStation, downStation);
    }

    public Stations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("상행종점역과 하행종점역의 아이디는 같을 수 없습니다.");
        }
        this.upStation = upStation;
        this.downStation = downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stations stations = (Stations) o;
        return Objects.equals(upStation, stations.upStation) && Objects.equals(downStation, stations.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }
}
