package subway.domain;

import subway.exception.StationNotFoundException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        sections.add(section);
    }

    public Long getTotalDistance() {
        return sections.stream()
                .mapToLong(section  -> section.getDistance())
                .sum();
    }

    public Station getDownStation() {
        return sections.get(0).getDownStation();
    }

    public Station getUpStation() {
        return sections.get(sections.size()-1).getUpStation();
    }
}
