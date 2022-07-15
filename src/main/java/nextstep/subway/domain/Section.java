package nextstep.subway.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    @Embedded
    private Stations stations;

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(final Station upStation, final Station downStation, Distance distance) {
        if (upStation.equals(downStation)) {
            throw new IllegalStateException("두 역이 같은 정보일 수 없습니다.");
        }
        this.stations = new Stations(upStation, downStation);
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Stations getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<Station> stations() {
        return stations.toList();
    }
}
