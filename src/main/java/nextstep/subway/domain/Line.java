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
    private Long distance;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private List<Station> stations;

    public Line() {
    }

    public Line(LineRequest lineRequest) {
        this.id = lineRequest.getId();
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.distance = lineRequest.getDistance();
        this.stations = to(lineRequest.getUpStationId(), lineRequest.getDownStationId());
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

    public Long getDistance() {
        return distance;
    }


    public List<Station> to(Long upStationId, Long downStationId) {
        return List.of(new Station(upStationId), new Station(downStationId));
    }
}
