package subway.jpa;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

//    @Column(name = "up_station_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

//    @Column(name = "down_station_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Column(nullable = false)
    private Integer distance;

    public Line() {}

    public Line(String name, String color, Station upStation, Station downStation, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }


    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
