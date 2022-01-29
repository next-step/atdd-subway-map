package nextstep.subway.domain;

import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import nextstep.subway.exception.CannotDeleteNotLastStation;
import nextstep.subway.exception.DistanceUnderZeroException;
import nextstep.subway.exception.SameStationsRegisteredSectionException;

@Entity
public class Section extends BaseEntity {

    private static final int MIN_DISTANCE_VALUE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Station upStation;

    @ManyToOne
    private Station downStation;

    private int distance;

    protected Section() {
    }

    public Section(Station upStation, Station downStation, int distane) {
        validateSection(upStation, downStation, distane);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distane;
    }

    private void validateSection(Station upStation, Station downStation, int distane) {
        if (upStation.equals(downStation)) {
            throw new SameStationsRegisteredSectionException(upStation.getName());
        }
        if (distane < MIN_DISTANCE_VALUE) {
            throw new DistanceUnderZeroException(distane);
        }
    }

    public boolean containStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        validateStation(station);
        return downStation.equals(station);
    }

    private void validateStation(Station station) {
        if (!downStation.equals(station)) {
            throw new CannotDeleteNotLastStation(station.getName());
        }
    }

    public Station getDownStation() {
        return downStation;
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }

}
