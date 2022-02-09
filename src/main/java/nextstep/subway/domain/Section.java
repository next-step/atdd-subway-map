package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Section extends BaseEntity{
    @Transient
    private static final String GIVEN_DOWN_STATION_IS_ALREADY_REGISTERED_IN_LINE = "이미 등록된 노선명입니다.";

    @Transient
    private static final String UP_STATION_OF_NEW_SECTION_MUST_BE_DOWN_STATION_OF_LINE = "새로운 구간의 상행역은 노선의 하행 종점역이어야만 합니다.";

    @Transient
    private static final String ONLY_LAST_STATION_OF_LINE_CAN_BE_DELETED = "지하철 노선의 마지막 역만 삭제신청할 수 있습니다.";

    @Transient
    private static final String SECTION_CANNOT_BE_DELETED_WHEN_LINE_HAS_ONLY_ONE_SECTION = "노선에 구간이 하나만 존재하는 경우, 구간은 삭제할 수 없습니다.";

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
        if(line.hasAnyStation()) {
            validateUpStation(line, upStation);
            validateDownStation(line, downStation);
        }
    }

    private void validateUpStation(Line line, Station upStation) {
        if (!line.isDownStation(upStation))  {
            throw new IllegalArgumentException(UP_STATION_OF_NEW_SECTION_MUST_BE_DOWN_STATION_OF_LINE);
        }
    }

    private void validateDownStation(Line line, Station downStation) {
        if (line.has(downStation))  {
            throw new IllegalArgumentException(GIVEN_DOWN_STATION_IS_ALREADY_REGISTERED_IN_LINE);
        }
    }

    public Long getUpStationId() {
        return this.upStation.getId();
    }

    public Long getDownStationId() {
        return this.downStation.getId();
    }

    public static void validateDeleteSectionRequest(Line line, Station station) {
        if (line.hasOnlyOneSection()) {
            throw new IllegalArgumentException(SECTION_CANNOT_BE_DELETED_WHEN_LINE_HAS_ONLY_ONE_SECTION);
        }

        if (!line.isDownStation(station)) {
            throw new IllegalArgumentException(ONLY_LAST_STATION_OF_LINE_CAN_BE_DELETED);
        }
    }
}