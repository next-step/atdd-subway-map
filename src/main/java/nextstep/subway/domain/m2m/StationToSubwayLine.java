package nextstep.subway.domain.m2m;

import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.subwayLine.SubwayLine;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class StationToSubwayLine {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "station_to_subway_line_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne(fetch = LAZY)
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
