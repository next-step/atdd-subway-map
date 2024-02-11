package subway.line.domain;

import subway.section.domain.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany
    @JoinColumn(name = "line_id")
    private final List<Section> sections = new ArrayList<>();

    public List<Long> getStationIds() {
        return sections.stream()
                    .map(Section::getDownStationId)
                    .collect(Collectors.toList());
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void deleteLastSection() {
        Section section = getLastSection();
        sections.remove(section);
    }

    public Long getUpStationId() {
        return sections.get(0).getUpStationId();
    }

    public Long getDownStationId() {
        if (sections.isEmpty()) {
            return null;
        }
        return sections.get(sections.size() - 1).getDownStationId();
    }

    public int getTotalDistance() {
        return sections.stream()
                    .mapToInt(Section::getDistance)
                    .sum();
    }

    private Section getLastSection() {
        if (sections.isEmpty()) {
            return null;
        }
        return sections.get(sections.size() - 1);
    }
}
