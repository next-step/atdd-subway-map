package nextstep.subway.domain.section;

import lombok.Getter;
import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    public Station upStation() {
        return this.sections.stream().findFirst().get().getUpStation();
    }

    public Station downStation() {
        return this.sections.size() > 0 ? this.sections.stream().reduce((first, second) -> second).orElseThrow(null).getDownStation() : null;
    }

    public Long distance() {
        return sections.stream().mapToLong(Section::getDistance).sum();
    }

    private void validAddUpStation(Station upStation) {
        if (!Objects.equals(downStation(), upStation)) {
            throw new IllegalArgumentException("section.upStation.line.downStation");
        }
    }

    private void validAddDownStation(Station addSectionDownStation) {
        Set<Long> sectionIds = sections.stream().map(section -> section.getUpStation().getId()).collect(Collectors.toSet());
        sections.stream().map(section -> section.getDownStation().getId()).forEach(sectionIds::add);
        if (sectionIds.contains(addSectionDownStation.getId())) {
            throw new IllegalArgumentException("section.downStation.line.duplicate");
        }
    }

    public void validDelete(Station downStation) {
        validDeleteUpStation(downStation);
        validSectionCount();
    }

    public Section createSection(Line line, Station upStation, Station downStation, Long distance) {
        Section section = new Section(line, upStation, downStation, distance);
        this.sections.add(section);
        return section;
    }

    public Section addSection(Line line, Station upStation, Station downStation, Long distance) {
        validAddUpStation(upStation);
        validAddDownStation(downStation);
        Section section = new Section(line, upStation, downStation, distance);
        this.sections.add(section);
        return section;
    }

    public void validSectionCount() {
        if (this.sections.size() < 2) {
            throw new IllegalArgumentException("section.count.less");
        }
    }

    public void validDeleteUpStation(Station downStation) {
        for (Section section : this.sections) {
            if (Objects.equals(section.getUpStation().getId(), downStation.getId())) {
                throw new IllegalArgumentException("section.upStation.not.delete");
            }
        }
    }
}
