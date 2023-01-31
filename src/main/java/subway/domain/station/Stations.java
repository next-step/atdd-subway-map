package subway.domain.station;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.line.Line;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stations {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Station> stations = new ArrayList<>();

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStationList() {
        return stations;
    }

    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }

    public void belongTo(Line line) {
        stations.forEach(station -> station.belongTo(line));
    }
}