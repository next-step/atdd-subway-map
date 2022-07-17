package nextstep.subway.domain.Line;

import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line() {

    }

    public Station upStation() {
        return this.sections.stream().findFirst().get().getUpStation();
    }

    public Station downStation() {
        return this.sections.size() > 0 ? this.sections.stream().reduce((first, second) -> second)
                .orElseThrow(null).getDownStation() : null;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public Long distance() {
        return sections.stream().mapToLong(Section::getDistance).sum();
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

    public Set<Section> sections() {
        return this.sections;
    }

}
