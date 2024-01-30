package subway.line;

import subway.station.Station;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Embedded
    private Section section;
    @Column(nullable = false)
    private Long distance;

    protected Line() {
    }

    public Line(String name,
                String color,
                Station upStation,
                Station downStation,
                Long distance) {
        this.name = name;
        this.color = color;
        this.section = new Section(upStation, downStation);
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

    public Section getStationLink() {
        return section;
    }

    public void update(String name,
                       String color) {
        this.name = name;
        this.color = color;
    }
}
