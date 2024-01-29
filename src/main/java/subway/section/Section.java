package subway.section;

import subway.line.Line;
import subway.station.Station;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int distance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station downStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Line line;

    public Section() {
    }

    private Section(int distance, Station upStation, Station downStation, Line line) {
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
    }

    public static Section of(int distance, Station upStation, Station downStation, Line line) {
        Section section = new Section(distance, upStation, downStation, line);
        line.getSections().add(section);
        return section;
    }

    public static Section verifyAndGenerate(int distance, Station upStation, Station downStation, Line line) {
        verifyUpStation(upStation, line);

        verifyAlreadyStation(downStation, line);

        return Section.of(distance, upStation, downStation, line);
    }

    private static void verifyUpStation(Station upStation, Line line) {
        boolean isNotLineDownStation = !line.getDownStation().getId().equals(upStation.getId());

        if (isNotLineDownStation) {
            throw new SectionException("등록할 구간의 상행역이 노선에 등록되어있는 하행종점역이 아닌 경우 구간 등록이 불가능합니다.");
        }

    }

    private static void verifyAlreadyStation(Station downStation, Line line) {
        boolean isAlreadyStation = line.getSections().stream().anyMatch(s ->
                s.getUpStation().getId().equals(downStation.getId()) ||
                        s.getDownStation().getId().equals(downStation.getId()));

        if (isAlreadyStation) {
            throw new SectionException("이미 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Line getLine() {
        return line;
    }
}
