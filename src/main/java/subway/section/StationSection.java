package subway.section;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import subway.line.StationLine;
import subway.station.Station;

@Entity
public class StationSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "station_line_id", nullable = false)
    private StationLine stationLine;

    @ManyToOne
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @Column
    private long distance;

    public StationSection() {}

    public StationSection(StationLine stationLine, Station upStation, Station downStation, long distance) {
        this.stationLine = stationLine;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean isExistSection(StationSection stationSection) {
        return this.upStation == stationSection.downStation;
    }

    public boolean isConnectedSection(StationSection stationSection) {
        return this.downStation == stationSection.upStation;
    }

    public long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public void remove() {
        stationLine.getStationSections().remove(this);
    }

    public boolean isMatchDownStation(Station station) {
        return this.downStation == station;
    }
}
