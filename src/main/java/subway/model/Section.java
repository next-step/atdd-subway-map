package subway.model;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private Long distance;

    protected Section() {

    }

    public Section(Station upStation, Station downStation, Long distance) {
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

    public boolean equals(Station upStation, Station downStation) {
        return this.upStation.equals(upStation) && this.downStation.equals(downStation);
    }

    public boolean contains(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public boolean equalToDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean equalUpStation(Station station) {
        return upStation.equals(station);
    }

    public static class Builder {
        private Station upStation;
        private Station downStation;
        private Long distance;

        public Builder() {
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
            return new Section(upStation, downStation, distance);
        }
    }
}
