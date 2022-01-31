package nextstep.subway.domain;

import nextstep.subway.exception.SameStationException;

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

    public Section() {

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

    public Section(Station upStation, Station downStation, int distance) {
        if (upStation == downStation) {
            throw new SameStationException("상행역과 하행역이 같을 수 없습니다.");
        }

        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void changeLine(Line line) {
        if (this.line != null ){
            this.line.getSections().remove(this);
        }

        this.line = line;

        if (!line.getSections().contains(this)) {
            line.getSections().add(this);
        }
    }
}
