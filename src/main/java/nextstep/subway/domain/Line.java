package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Embedded
    private Sections sections;

    private String name;
    private String color;

    protected Line() {
    }

    private Line(String name, String color, Sections sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line create(String name, String color, Section section) {
        return new Line(name, color, new Sections(section));
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String color() {
        return color;
    }

    public List<Station> stations() {
        return sections.toList();
    }
}
