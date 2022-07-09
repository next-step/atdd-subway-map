package nextstep.subway.domain;

import nextstep.subway.domain.exception.InvalidUpDownStationException;

import javax.persistence.*;
import java.util.List;

@Entity
public class Section {

    private static final int INDEX_UP_STATION = 0;
    private static final int INDEX_DOWN_STATION = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY)
    private List<Station> stations;

    @Embedded
    private Distance distance;

    protected Section() {
    }

    private Section(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = new Distance(distance);
    }

    public static Section create(Station upStation, Station downStation, int distance) {
        if (upStation.equals(downStation)) {
            throw new InvalidUpDownStationException();
        }
        return new Section(List.of(upStation, downStation), distance);
    }

    public List<Station> stations() {
        return stations;
    }
}
