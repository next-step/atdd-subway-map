package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.exception.InvalidUpDownStationException;

import javax.persistence.*;
import java.util.List;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stations {

    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;

    private Stations(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static Stations create(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new InvalidUpDownStationException();
        }
        return new Stations(upStation, downStation);
    }

    public List<Station> toList() {
        return List.of(upStation, downStation);
    }
}
