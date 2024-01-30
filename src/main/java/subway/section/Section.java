package subway.section;

import subway.line.Line;
import subway.station.Station;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UP_STATION_ID")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOWN_STATION_ID")
    private Station downStation;
    @Column(nullable = false)
    private int distance;

    public Section() {
    }

    public Section(final Station upStation, final Station downStation, final int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }

    // 연관관계 편의 메소드
    public void setLine(final Line line) {
        this.line = line;
        // 기존 팀과 연관관계를 제거
        if (this.line != null) {
            this.line.getSections().remove(this);
        }

        // 새로운 연관관계 설정
        this.line = line;
        if (line != null) { // 연관관계 제거 시, team == null
            line.getSections().add(this);
        }
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", line=" + line +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }

    public void changeDownStation(final Station downStation) {
        this.downStation = downStation;
    }
}
