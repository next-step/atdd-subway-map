package nextstep.subway.line.domain.model;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import nextstep.subway.common.domain.model.BaseEntity;
import nextstep.subway.station.domain.model.Station;

@Entity
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Line line;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Station upStation;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Station downStation;

    @Column
    @AttributeOverride(
        name = "value",
        column = @Column(name = "distance")
    )
    private Distance distance;

    protected Section() {
    }

    private Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean matchUpStation(Station thatStation) {
        return upStation.matchId(thatStation);
    }

    public boolean matchDownStation(Station thatStation) {
        return downStation.matchId(thatStation);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Line line;
        private Station upStation;
        private Station downStation;
        private Distance distance;

        public Builder line(Line line) {
            this.line = line;
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(Distance distance) {
            this.distance = distance;
            return this;
        }

        public Section build() {
            return new Section(line, upStation, downStation, distance);
        }
    }
}
