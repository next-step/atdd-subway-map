package subway.line.domain;

import java.util.Arrays;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import subway.section.domain.SectionStations;
import subway.station.domain.Station;

@Embeddable
public class LineLastStations {
    @ManyToOne
    private Station upLastStation;

    @ManyToOne
    private Station downLastStation;

    protected LineLastStations() {}

    public LineLastStations(Station upLastStation, Station downLastStation) {
        if (upLastStation.equals(downLastStation)) {
            throw new IllegalArgumentException();
        }

        this.upLastStation = upLastStation;
        this.downLastStation = downLastStation;
    }

    public List<Station> getStations() {
        return Arrays.asList(upLastStation, downLastStation);
    }

    public Station getUpLastStation() {
        return upLastStation;
    }

    public Station getDownLastStation() {
        return downLastStation;
    }

    public void updateDownLastStation(Station station) {
        this.downLastStation = station;
    }

    public boolean checkCanAddSection(SectionStations sectionStations) {
        if (!downLastStation.equals(sectionStations.getUpStation())) {
            return false;
        }

        if (upLastStation.equals(sectionStations.getDownStation())) {
            return false;
        }

        return true;
    }

    public boolean isLastDownwardStation(Station station) {
        return downLastStation.equals(station);
    }
}
