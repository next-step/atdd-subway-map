package subway.section;

import subway.line.SubwayLine;
import subway.station.Station;

import javax.persistence.*;

@Entity
public class SubwaySection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    @ManyToOne
    @JoinColumn(name = "subway_line_id")
    private SubwayLine line;

    public SubwaySection() {}

    public SubwaySection(Station upStation, Station downStation, SubwayLine line) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
    }

    public Long getId() { return id; }

    public Station getUpStation() { return upStation; }

    public Station getDownStation() { return downStation; }

    public SubwayLine getLine() { return line; }
}