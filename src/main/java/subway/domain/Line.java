package subway.domain;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    private String color;

    private int distance;

    @OneToOne
    @JoinColumn(name = "up_staion_id")
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "down_staion_id")
    private Station downStation;

    protected Line() {
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
