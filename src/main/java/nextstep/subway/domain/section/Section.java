package nextstep.subway.domain.section;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.dto.StationResponse;
import nextstep.subway.handler.validator.SectionValidator;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Station downStation;

    private int distance;

    public Section() {
    }

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        SectionValidator.proper(line, upStation, downStation, distance);
        return new Section(line, upStation, downStation, distance);
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
}
