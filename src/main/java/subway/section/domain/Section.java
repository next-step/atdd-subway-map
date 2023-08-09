package subway.section.domain;

import lombok.*;
import subway.common.BaseEntity;
import subway.line.domain.Line;
import subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Section extends BaseEntity {
    @ManyToOne
    @Setter
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Station downStation;

    private int distance;

    @Builder
    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Line getLine() {
        return this.line;
    }

    public Long getDownStationId() {
        return downStation.getId();
    }
}
