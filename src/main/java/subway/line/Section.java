package subway.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.Station;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;
    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    private Integer distance;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line")
    private Line line;

    public Section(final Station downStation, final Station upStation, final Integer distance) {
        this.downStation = downStation;
        this.upStation = upStation;
        this.distance = distance;
    }

    public void addLine(final Line line) {
        this.line = line;
    }
}
