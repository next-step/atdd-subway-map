package nextstep.subway.domain.Line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.Sections;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Set<Section> sections() {
        return this.sections.sections();
    }

    public Long distance() {
        return this.sections.distance();
    }

    public Station upStation() {
        return this.sections.upStation();
    }

    public Station downStation() {
        return this.sections.downStation();
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void validAddSection(Station upStation, Station downStation) {
        validUpStation(upStation);
        validDownStation(downStation);
    }

    public void validUpStation(Station addSectionUpStation) {
        if (!Objects.equals(sections.downStation(), addSectionUpStation)) {
            throw new IllegalArgumentException("section.upStation.line.downStation");
        }
    }

    private void validDownStation(Station addSectionDownStation) {
        Set<Long> sectionIds = sections.sections().stream().map(section -> section.getUpStation().getId()).collect(Collectors.toSet());
        sections.sections().stream().map(section -> section.getDownStation().getId()).forEach(sectionIds::add);
        if (sectionIds.contains(addSectionDownStation.getId())) {
            throw new IllegalArgumentException("section.downStation.line.duplicate");
        }
    }

    public void validDeleteDownStation(Station downStation) {
        validDeleteUpStation(downStation);
        validSectionCount();
    }

    public void validDeleteUpStation(Station downStation) {
        this.sections.validDeleteUpStation(downStation);
    }

    public void validSectionCount() {
        if (this.sections.sections().size() < 2) {
            throw new IllegalArgumentException("section.count.less");
        }
    }
}
