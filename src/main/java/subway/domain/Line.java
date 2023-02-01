package subway.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.StringUtils;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Embedded
    private Stations stations;

    @Embedded
    private Distance distance;

    protected Line() {
    }

    public Line(final String name, final String color, final List<Station> stations, final int distance) {
        this.name = name;
        this.color = color;
        this.stations = new Stations(stations);
        this.distance = new Distance(distance);
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
        return Collections.unmodifiableList(stations.getStations());
    }

    public int getDistance() {
        return distance.getValue();
    }

    public Line editName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
        this.name = name;
        return this;
    }

    public Line editColor(final String color) {
        if (!StringUtils.hasText(color)) {
            throw new IllegalArgumentException("색상은 공백일 수 없습니다.");
        }
        this.color = color;
        return this;
    }

    public Line editDistance(final int distance) {
        this.distance = new Distance(distance);
        return this;
    }

    public void detachStations() {
        stations.detach();
    }
}
