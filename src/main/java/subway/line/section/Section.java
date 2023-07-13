package subway.line.section;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import subway.line.Line;
import subway.station.Station;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;
    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;
    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;
    private int sequence;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation) {
        this(null, line, upStation, downStation, 1);
    }

    public Section(Line line, Station upStation, Station downStation, int sequence) {
        this(null, line, upStation, downStation, sequence);
    }

    public Section(Long id, Line line, Station upStation, Station downStation, int sequence) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.sequence = sequence;
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

    public int getSequence() {
        return sequence;
    }
}
