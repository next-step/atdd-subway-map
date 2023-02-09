package subway.station.domain.section;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.line.Line;
import subway.station.domain.station.Station;

import javax.persistence.*;

@Getter
@Entity
public class Section {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SECTION_ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "STATION_ID")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "STATION_ID")
    private Station downStation;
    private Long distance;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(referencedColumnName = "LINE_ID")
    private Line line;

    public Section() {
    }

    @Builder
    public Section(Station upStation, Station downStation, Long distance, Line line) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }
}
