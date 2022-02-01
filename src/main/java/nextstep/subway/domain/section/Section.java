package nextstep.subway.domain.section;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.dto.StationResponse;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;

    private int distance;

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

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }

    public boolean hasDownStation(Station target) {
        return downStation.equals(target);
    }

    public boolean hasStation(Station downStation) {
        return (this.upStation.equals(downStation) || this.downStation.equals(downStation));
    }

    public void push(List<Station> stations) {
        if (!stations.contains(upStation)) {
            stations.add(upStation);
        }
        stations.add(downStation);
    }

    public List<StationResponse> getStationsResponse() {
        return Arrays.asList(StationResponse.from(upStation), StationResponse.from(downStation));
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Station getUpStation() {
        return upStation;
    }
}
