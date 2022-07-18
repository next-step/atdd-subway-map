package nextstep.subway.domain.Line;

import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void validAddSection(Station upStation, Station downStation) {
        validUpStation(upStation);
        validDownStation(downStation);
    }

    public void validUpStation(Station sectionUpStation) {
        if (!Objects.equals(this.downStation().getId(), sectionUpStation.getId())) {
            throw new IllegalArgumentException("section.upStation.line.downStation");
        }
    }

    private void validDownStation(Station downStation) {
        Set<Long> sectionIds = this.sections().stream().map(section -> section.getUpStation().getId()).collect(Collectors.toSet());
        this.sections().stream().map(section -> section.getDownStation().getId()).forEach(sectionIds::add);
        if (sectionIds.contains(downStation.getId())) {
            throw new IllegalArgumentException("section.downStation.line.duplicate");
        }
    }

}
