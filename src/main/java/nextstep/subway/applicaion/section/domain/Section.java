package nextstep.subway.applicaion.section.domain;

import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.station.domain.Station;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    public Station upStation;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    public Station downStation;
    public Integer distance;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    public Line line;

    public Section(Station upStation, Station downStation, Integer distance, Line line) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }
}
