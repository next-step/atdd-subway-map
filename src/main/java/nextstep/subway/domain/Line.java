package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineRequest;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int distance;
    @Embedded
    private Stations stations;

    protected Line() {/*no-op*/}

    public Line(Long id, String name, String color, int distance, List<Station> stations) {

        if (name == null || name.isBlank() || name.length() < 2) {
            throw new IllegalArgumentException("이름이 공백이거나 2글자 이하일 수 없습니다.");
        }

        if (color.isBlank() || color.length() < 4) {
            throw new IllegalArgumentException("색이 공백이거나 4글자 이하일 수 없습니다.");
        }

        if (distance < 1) {
            throw new IllegalArgumentException("거리는 1 이상의 자연수 입니다.");
        }

        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = new Stations(stations);
    }

    public Line(String name, String color, int distance, List<Station> stations) {
        this(null, name, color, distance, stations);
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

    public Stations getStations() {
        return stations;
    }

    public void edit(LineRequest lineRequest) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
    }
}
