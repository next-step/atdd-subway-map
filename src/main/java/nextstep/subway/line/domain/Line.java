package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(
        String name, String color, Station upStation,
        Station downStation, int distance) {
        Line line = new Line();
        line.name = name;
        line.color = color;
        line.sections.add(null);
        return line;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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
}
