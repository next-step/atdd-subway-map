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

    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;

    @Embedded
    private Distance distance;

    private Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section create(Station upStation, Station downStation, int distance) {
        if (upStation.equals(downStation)) {
            throw new InvalidUpDownStationException();
        }
        return new Section(upStation, downStation, new Distance(distance));
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Long id() {
        return id;
    }

    public Station upStation() {
        return upStation;
    }

    public Station downStation() {
        return downStation;
    }

    public boolean hasStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public List<Station> stations() {
        return List.of(upStation, downStation);
    }

    public boolean isMatchDownStation(Station station) {
        return downStation.equals(station);
    }
}
