package nextstep.subway.section;

import jdk.nashorn.internal.objects.annotations.Getter;
import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "up_station_id")
    @ManyToOne
    private Station upStation;

    @JoinColumn(name = "down_station_id")
    @ManyToOne
    private Station downStation;
    private int distance;

    @JoinColumn(name = "line_id")
    @ManyToOne
    private Line line;

    public Section() {
    }

    private Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Station upStation, Station downStation, int distance) {
        return new Section(upStation, downStation, distance);
    }

    public void addLine(Line line) {
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Station getDownStation() {
        return downStation;
    }

    public boolean isExistStation(Station downStation) {
        return upStation.equals(downStation) || downStation.equals(downStation);
    }
}
