package nextstep.subway.domain.m2m;

import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.subwayLine.SubwayLine;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;

@Entity
public class StationToSubwayLine {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "subway_line_id")
    private SubwayLine subwayLine;

    public StationToSubwayLine() {
    }

    public StationToSubwayLine(SubwayLine subwayLine, Station station) {
        this.subwayLine = subwayLine;
        this.station = station;
    }

    public Station getStation() {
        return station;
    }

    public SubwayLine getSubwayLine() {
        return subwayLine;
    }
}
