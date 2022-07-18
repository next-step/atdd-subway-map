package nextstep.subway.domain.section;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToOne()
    @JoinColumn(name = "up_station_id", referencedColumnName = "station_id")
    private Station upStation;

    @OneToOne()
    @JoinColumn(name = "down_station_id", referencedColumnName = "station_id")
    private Station downStation;

    private Long distance;

    public Section(Line line, Station upStation, Station downStation, Long distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

}
