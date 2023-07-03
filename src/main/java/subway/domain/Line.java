package subway.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Column(nullable = false)
    private Long distance;

    private Line(final String name, final String color, final Station upStation, final Station downStation,
                 final Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public static LineBuilder builder() {
        return new LineBuilder();
    }

    public static class LineBuilder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private Long distance;

        public LineBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public LineBuilder color(final String color) {
            this.color = color;
            return this;
        }

        public LineBuilder upStation(final Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public LineBuilder downStation(final Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public LineBuilder distance(final Long distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(
                    Objects.requireNonNull(name),
                    Objects.requireNonNull(color),
                    Objects.requireNonNull(upStation),
                    Objects.requireNonNull(downStation),
                    Objects.requireNonNull(distance)
            );
        }
    }
}
