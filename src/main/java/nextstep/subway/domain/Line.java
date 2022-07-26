package nextstep.subway.domain;

import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Station> stations = new ArrayList<>();

    private Long distance;

    public Line(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.stations.add(upStation);
        this.stations.add(downStation);
        this.distance = lineRequest.getDistance();
        upStation.setLine(this);
        downStation.setLine(this);
    }

    public Line(Long id, String name, String color, List<Station> stations, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getUpStationId() { return this.stations.get(0).getId(); }

    public Long getDownStationId() { return this.stations.get(1).getId(); }

    public void update(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName() != null ? lineRequest.getName() : this.name;
        this.color = lineRequest.getColor() != null ? lineRequest.getColor() : this.color;
        this.stations.set(0, upStation != null ? upStation : this.stations.get(0));
        this.stations.set(1, downStation != null ? downStation : this.stations.get(1));
        this.distance = lineRequest.getDistance() != null ? lineRequest.getDistance() : this.distance;
    }
}
