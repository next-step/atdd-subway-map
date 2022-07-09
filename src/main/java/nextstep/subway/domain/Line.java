package nextstep.subway.domain;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private Set<Section> sections = new LinkedHashSet<>();

    private String name;
    private String color;

    protected Line() {
    }

    private Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.sections.add(section);
    }

    public static Line create(String name, String color, Section section) {
        return new Line(name, color, section);
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
        return sections.stream()
                .map(Section::stations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
