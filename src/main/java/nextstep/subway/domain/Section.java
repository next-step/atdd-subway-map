package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.exception.InvalidUpDownStationException;

import javax.persistence.*;
import java.util.List;

@Entity
@EqualsAndHashCode(exclude = "line")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @Embedded
    private Stations stations;

    @Embedded
    private Distance distance;

    private Section(Stations stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Section create(Station upStation, Station downStation, int distance) {
        if (upStation.equals(downStation)) {
            throw new InvalidUpDownStationException();
        }
        return new Section(new Stations(upStation, downStation), new Distance(distance));
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<Station> stations() {
        return stations.toList();
    }
}
