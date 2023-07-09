package subway.section.domain;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import subway.line.domain.LineLastStations;
import subway.station.domain.Station;

@Embeddable
public class SectionStations {

    @ManyToOne
    private Station upStation;

    @ManyToOne
    private Station downStation;

    protected SectionStations() {}

    public SectionStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException();
        }
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static SectionStations createLineBaseSection(LineLastStations lineLastStations) {
        return new SectionStations(lineLastStations.getUpLastStation(),
                lineLastStations.getDownLastStation());
    }

    public static SectionStations createSectionStations(List<Station> stations) {
        if (stations.size() != 2) {
            throw new IllegalArgumentException();
        }
        return new SectionStations(stations.get(0), stations.get(1));
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
