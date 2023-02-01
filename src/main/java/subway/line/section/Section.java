package subway.line.section;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.Line;
import subway.station.Station;

@Entity
@Table(name = "SECTION")
@NoArgsConstructor
@Getter
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LINE_ID")
    private Line line;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOWN_STATION_ID")
    private Station downStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UP_STATION_ID")
    private Station upStation;

    private int distance;

    private int number;

    public void setUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public Section(Line line, Station downStation, Station upStation, int distance) {
        this.line = line;
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public boolean isConnected(Section other) {
        return this.downStation == other.upStation;
    }
}
