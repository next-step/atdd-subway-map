package subway.line.domain;

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

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private LineLastStations lastStations;

    protected Line() {}

    public Line(String name, String color, LineLastStations lastStations) {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(color)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.color = color;
        this.lastStations = lastStations;
    }

    public void updateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
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

    public void updateColor(String color) {
        if (!StringUtils.hasText(color)) {
            throw new IllegalArgumentException();
        }
        this.color = color;
    }

    public LineLastStations getLastStations() {
        return lastStations;
    }
}
