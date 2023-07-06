package subway.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.dto.LineRequest;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(length = 20, nullable = false)
    private Long distance;

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY)
    List<Station> stations;

    public Line() {

    }

    public Line(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.distance = lineRequest.getDistance();
        this.stations = List.of(upStation, downStation);
    }

    public void updateLine(LineRequest lineRequest) {
        this.name = lineRequest.getName().isBlank() ? this.name : lineRequest.getName();
        this.color = lineRequest.getColor().isBlank() ? this.color : lineRequest.getColor();
        this.distance = lineRequest.getDistance() == null ? this.distance : lineRequest.getDistance();
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

    public List<Station> getStations() {
        return stations;
    }
}
