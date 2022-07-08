package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;
    @Embedded
    private Distance distance;
    @Embedded
    private Stations stations;

    public void changeInfo(String name, String color) {
        validation(name, color);
        this.name = name;
        this.color = color;
    }

    private void validation(String name, String color) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("노선의 이름을 작성해주세요");
        }

        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("색상을 넣어 주세요");
        }
    }

    protected Line() {
    }

    public Line(String name, String color, long distance, Station upStation, Station downStation) {
        validation(name, color);
        this.name = name;
        this.color = color;
        this.distance = new Distance(distance);
        this.stations = new Stations(upStation, downStation);
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

    public Distance getDistance() {
        return distance;
    }

    public Stations getStations() {
        return stations;
    }


}
