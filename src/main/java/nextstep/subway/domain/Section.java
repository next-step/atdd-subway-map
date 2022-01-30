package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Eager로 가져오는 게 맞을까?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LINE_ID")
    private Line line;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UP_STAION_ID")
    private Station upStation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOWN_STAION_ID")
    private Station downStation;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Long getId() {
        return id;
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
}
