package nextstep.subway.domain;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public void setLine(Line line) {
        this.line = line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Section() {
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void downStationIsNot(Long stationId) {
        if (downStation.isNotSameStation(stationId)) {
            throw new IllegalArgumentException("노선과 연결되지 않아 추가할 수 없습니다.");
        }
    }

    public static Section of(Station upStation, Station downStation, int distance){
        return new Section(upStation, downStation, distance);
    }

    public List<Long> getStationIds() {
        return Arrays.asList(upStation.getId(),downStation.getId());
    }



}