package subway.line;

import subway.station.Station;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "line")
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Line_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    /**
     * JPA를 위한 기본 생성자
     */
    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Line of(LineRequest lineRequest, Station upStation, Station downStation) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                upStation,
                downStation,
                lineRequest.getDistance()
        );
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

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return id.equals(line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
