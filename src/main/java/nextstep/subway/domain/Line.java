package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineRequest;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Stations stations;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        stations = new Stations();
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

    public Stations getStations() {
        return stations;
    }

    public void update(LineRequest lineInfo) {
        this.name = lineInfo.getName();
        this.color = lineInfo.getColor();
    }
}
