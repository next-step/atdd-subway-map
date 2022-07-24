package nextstep.subway.domain;

import lombok.Getter;
import nextstep.subway.applicaion.exceptions.InvalidStationParameterException;
import nextstep.subway.enums.exception.ErrorCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    private Integer distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Integer distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    List<Station> getRelatedStations() {
        return List.of(upStation, downStation);
    }

    public void validSameReqUpStationAndReqDownStation(Station upStation, Station downStation) {
        if (Objects.equals(upStation.getName(), downStation.getName()))
            throw new InvalidStationParameterException(ErrorCode.NOT_SAME_STATION);
    }
}
