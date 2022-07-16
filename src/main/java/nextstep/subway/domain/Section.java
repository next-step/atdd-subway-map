package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer distance;

    @ManyToOne(fetch = FetchType.LAZY)
    private StationLine stationLine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @Builder
    public Section(StationLine stationLine, Station upStation, Station downStation, Integer distance) {
        this.stationLine = stationLine;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void updateStationLine(StationLine stationLine) {
        this.stationLine = stationLine;
    }

}
