package nextstep.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    protected Sections() {
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public List<Station> stations() {
        return sections.stream()
            .map(Section::stations)
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toUnmodifiableList());
    }
}
