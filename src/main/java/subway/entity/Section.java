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

    public Section(Station upStation, Station downStation, Integer distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    static public Section of(List<Station> stations, Integer distance) {
        return new Section(stations.get(0), stations.get(1), distance);
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
}
