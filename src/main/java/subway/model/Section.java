package subway.model;

import subway.exception.AddSectionException;
import subway.exception.ErrorMessage;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private Long distance;

    protected Section() {

    }

    public Section(Line line, Station upStation, Station downStation, Long distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    public void validate(Line line) {
        if (!line.equalToDownStation(upStation)) {
            throw new AddSectionException(ErrorMessage.WRONG_UPSTATION_ID);
        }
        if (line.includes(downStation)) {
            throw new AddSectionException(ErrorMessage.WRONG_DOWNSTATION_ID);
        }
    }

    public static class Builder {
        private Line line;
        private Station upStation;
        private Station downStation;
        private Long distance;

        public Builder() {
        }

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

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Section build() {
            return new Section(line, upStation, downStation, distance);
        }
    }
}
