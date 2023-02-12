package subway;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20)
    private String color;
    @ManyToOne
    private Station upStation;
    @ManyToOne
    private Station downStation;
    private Long distance;

    public Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Line from(LineRequest lineRequest) {
        return new Line(lineRequest.getName(),
                lineRequest.getColor(),
                new Station(lineRequest.getUpStationId()),
                new Station(lineRequest.getDownStationId()),
                lineRequest.getDistance());
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
}
