package subway.domain.line;

import subway.domain.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Embeddable
public class LineStations {

    @OneToMany(fetch = LAZY, mappedBy = "line", cascade = ALL)
    private List<LineStation> stations = new ArrayList<>();

    protected LineStations() {
    }

    public LineStations(Line line, Station upStation, Station downStation) {
       stations.add(new LineStation(line, upStation));
       stations.add(new LineStation(line, downStation));
    }

    public List<Station> getStations() {
        return stations.stream()
                .map(LineStation::getStation)
                .collect(Collectors.toList());
    }
}
