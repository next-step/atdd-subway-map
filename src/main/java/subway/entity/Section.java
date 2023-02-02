package subway.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn
    private Station upStation;

    @OneToOne
    @JoinColumn
    private Station downStation;

    @Column
    private Integer distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Line line;

    protected Section() {
    }

    public Section(Station upStation, Station downStation, Integer distance, Station downEndStation, Line line) {
        checkNewDownStationIsAleardyRegistered(downStation, line);
        checkNewUpStationIsDownEndStation(upStation, downEndStation);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void checkNewDownStationIsAleardyRegistered(Station downStation, Line line) {
        if (line.getAllStations().contains(downStation)) throw new IllegalStateException("해당 노선에 이미 등록된 역을 새로운 구간의 하행역으로 등록할 수 없습니다.");
    }

    private void checkNewUpStationIsDownEndStation(Station newUpStation, Station downEndStation) {
        if (downEndStation == null) return;
        if (!downEndStation.equals(newUpStation)) throw new IllegalStateException("하행 종점역이 아닌 역을 새로운 구간의 상행역으로 등록할 수 없습니다.");
    }

    static public Section create(List<Station> newStations, Integer distance, Station downEndStation, Line line) {
        return new Section(newStations.get(0), newStations.get(1), distance, downEndStation, line);
    }

    static public Section createForNewLine(List<Station> newStations, Integer distance, Line line) {
        return new Section(newStations.get(0), newStations.get(1), distance, null, line);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }

    public void initLine(Line line) {
        this.line = line;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }
}
