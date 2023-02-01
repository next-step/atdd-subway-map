package subway.line.station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.Line;
import subway.station.Station;

@Entity
@Table(name = "LINE_STATION")
@NoArgsConstructor
@Getter
public class LineStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LINE_ID")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATION_ID")
    private Station station;

    public LineStation(Line line, Station station) {
        this.line = line;
        this.station = station;
    }

    public Long getStationId() {
        return station.getId();
    }

    public void changeLine(Line line) {
        if (this.line == line) {
            return;
        }

        if (this.line != null) {
            this.line.removeLineStation(this);
        }

        this.line = line;
    }
}
