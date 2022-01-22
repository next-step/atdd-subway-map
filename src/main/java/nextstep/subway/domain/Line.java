package nextstep.subway.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public List<Station> getStations() {
        return stations.getStations();
    }

    public void update(String updateName, String updateColor) {
        this.name = updateName;
        this.color = updateColor;
    }
}
