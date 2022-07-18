package nextstep.subway.domain.section;

import nextstep.subway.domain.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    public void add(Section section) {
        this.sections.add(section);
    }

    public Station upStation() {
        return this.sections.stream().findFirst().get().getUpStation();
    }

    public Station downStation() {
        return this.sections.size() > 0 ? this.sections.stream().reduce((first, second) -> second)
                .orElseThrow(null).getDownStation() : null;
    }

    public Long distance() {
        return sections.stream().mapToLong(Section::getDistance).sum();
    }

    public Set<Section> sections() {
        return this.sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void validDeleteUpStation(Station downStation) {
        for (Section section : this.sections) {
            if (Objects.equals(section.getUpStation().getId(), downStation.getId())) {
                throw new IllegalArgumentException("section.upStation.not.delete");
            }
        }
    }
}
