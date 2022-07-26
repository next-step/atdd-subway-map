package nextstep.subway.domain.line;

import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToOne
    private Station upStation;
    @OneToOne
    private Station downStation;
    private Long distance;

    public Line() {
    }

    public Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.upStation = builder.upStation;
        this.downStation = builder.downStation;
        this.distance = builder.distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
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

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private Long distance;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder color(String val) {
            color = val;
            return this;
        }

        public Builder upStation(Station val) {
            upStation = val;
            return this;
        }

        public Builder downStation(Station val) {
            downStation = val;
            return this;
        }

        public Builder distance(Long val) {
            distance = val;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }
}
