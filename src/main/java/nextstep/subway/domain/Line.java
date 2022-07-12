package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineUpdateRequest;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    public Line() {
    }

    @OneToMany
    @JoinColumn(name = "line_id")
    private List<Station> stations;

    public Line(String name, String color, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Line changeFrom(LineUpdateRequest lineUpdateRequest) {
        this.name = lineUpdateRequest.getName();
        this.color = lineUpdateRequest.getColor();
        return this;
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

    public List<Station> getStations() {
        return stations;
    }
}
