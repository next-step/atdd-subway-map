package subway.domain.vo;

import subway.domain.Section;
import subway.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {

    @JoinColumn(name = "section_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public Station getUpStation() {
        return sections.get(0).getUpStation();
    }

    public Station getDownStation() {
        return sections.get(getLastIndex()).getDownStation();
    }

    private int getLastIndex() {
        return sections.size() - 1;
    }

    public boolean isNotEndWith(Station newSectionUpStation) {
        return !this.endWith(newSectionUpStation);
    }

    public boolean endWith(Station newSectionUpStation) {
        return Objects.equals(getDownStation(), newSectionUpStation);
    }

    public boolean contains(Station newSectionDownStation) {
        return this.sections.stream()
                .anyMatch(section -> Objects.equals(section.getUpStation(), newSectionDownStation)
                        || Objects.equals(section.getDownStation(), newSectionDownStation));
    }

    public Long sumOfDistance() {
        return this.sections.stream().mapToLong(Section::getDistance).sum();
    }
}
