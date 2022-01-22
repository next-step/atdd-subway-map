package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Stations {

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Station> stations = new ArrayList<>();

    protected Stations() {
    }

    public List<Station> getStations() {
        return stations;
    }
}
