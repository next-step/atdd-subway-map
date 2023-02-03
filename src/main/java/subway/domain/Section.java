package subway.domain;

import subway.exception.SectionException;

import javax.persistence.*;
import java.util.List;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    protected Section() {
    }

    public Section(Station upStation, Station downStation, int distance) {
        validate(upStation, downStation);

        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validate(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new SectionException("상행역과 하행역이 같은 구간은 생성할 수 없습니다.");
        }
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isDownStationId(long stationId) {
        return downStation.getId() == stationId;
    }

    public boolean addable(Section lastSection) {
        return upStation.equals(lastSection.downStation);
    }

    public boolean addable(List<Station> stations) {
        return !stations.contains(this.downStation);
    }
}
