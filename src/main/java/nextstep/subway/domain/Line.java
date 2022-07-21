package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @Column(name = "line_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public void addSection(Section section) {
        section.line(this);
        sections.addSection(section);
    }

    public void removeSection(Station findStation) {
        sections.removeSection(findStation);
    }

    public void change(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> allStations() {
        return sections.allStations();
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
