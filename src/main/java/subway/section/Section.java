package subway.section;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.Station;

@Entity
@Table(
    name = "SECTION",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UNIQUE_DOWN_UP_STATION_ID",
            columnNames = {"DOWN_STATION_ID", "UP_STATION_ID"}
        )
    }
)
@NoArgsConstructor
@Getter
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOWN_STATION_ID")
    private Station downStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UP_STATION_ID")
    private Station upStation;

    private int distance;

    public void setUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public Section(Station upStation, Station downStation, int distance) {
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

}
