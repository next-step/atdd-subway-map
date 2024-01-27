package subway.line;

import subway.station.Station;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private int distance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station downStation;

    public Line() {
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
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

    public int getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
