package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public static Section createOf(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public void validationUpStation(Section section) {
        if (this.downStation != section.getUpStation()) {
            throw new IllegalArgumentException("추가되는 구간의 상행선은 현재 등록되어있는 하행 종점역이여야 합니다.");
        }
    }

    public void validationDownStation(Section section) {
        if (this.downStation == section.getDownStation() || this.upStation == section.getDownStation()) {
            throw new IllegalArgumentException("추가되는 구간의 하행역은 현재 등록되어있는 역일 수 없습니다.");
        }
    }

    public void validationDeleteStation(Long stationId) {
        if (!this.downStation.equals(stationId)) {
            throw new IllegalArgumentException("마지막 하행 지하철역만 삭제 할수 있습니다.");
        }
    }
}