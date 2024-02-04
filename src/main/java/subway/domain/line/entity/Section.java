package subway.domain.line.entity;

import subway.domain.station.entity.Station;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Station upStation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Station downStation;

    private Long distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    public Section(){}
    public Section(Station upStation, Station downStation, Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }
}
