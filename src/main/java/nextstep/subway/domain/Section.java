package nextstep.subway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.BadRequestException;

import javax.persistence.*;

import static nextstep.subway.exception.SectionException.GIVEN_DOWN_STATION_IS_ALREADY_REGISTERED_IN_LINE;
import static nextstep.subway.exception.SectionException.UP_STATION_OF_NEW_SECTION_MUST_BE_DOWN_STATION_OF_LINE;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public Section(Line line, Station upStation, Station downStation) {
        validate(line, upStation, downStation);
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    private void validate(Line line, Station upStation, Station downStation) {
        if (line.hasAnyStation()) {
            validateUpStation(line, upStation);
            validateDownStation(line, downStation);
        }
    }

    private void validateUpStation(Line line, Station upStation) {
        if (!line.isDownStation(upStation)) {
            throw new BadRequestException(UP_STATION_OF_NEW_SECTION_MUST_BE_DOWN_STATION_OF_LINE);
        }
    }

    private void validateDownStation(Line line, Station downStation) {
        if (line.has(downStation)) {
            throw new BadRequestException(GIVEN_DOWN_STATION_IS_ALREADY_REGISTERED_IN_LINE);
        }
    }

    public Long getUpStationId() {
        return this.upStation.getId();
    }

    public Long getDownStationId() {
        return this.downStation.getId();
    }
}