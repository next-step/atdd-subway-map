package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    static Sections of(List<Section> sections) {
        return new Sections(sections);
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Long getUpStationId() {
        return sections.get(0).getUpStationId();
    }

    public Long getDownStationId() {
        return sections.get(sections.size() - 1).getDownStationId();
    }

    public Long getTotalDistance() {
        return sections.stream().mapToLong(Section::getDistance).sum();
    }

    public void add(Section section) {
        sections.add(section);
    }

    boolean containsStationId(Long stationId) {
        return sections.stream()
                .anyMatch(section -> section.containsStationId(stationId));
    }
}
