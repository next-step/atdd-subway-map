package subway.domain;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "downStation_id")
    private Station downStation;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "upStation_id")
    private Station upStation;

    private Long distance;

    public Long getId() {
        return id;
    }

    public Long getDistance() {
        return distance;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    protected Section() {

    }
    public Section(Station downStation, Station upStation ,Long distance) {
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
    }
}
