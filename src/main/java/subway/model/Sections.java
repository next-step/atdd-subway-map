package subway.model;

import subway.exception.DeleteSectionException;
import subway.exception.ErrorMessage;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "section_id")
    private List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean includes(Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    public boolean equalToLastDownStation(Station station) {
        Section lastSection = sections.get(sections.size() - 1);
        return lastSection.equalToDownStation(station);
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public void deleteLastSection(Station station) {
        if (this.sections.size() == 1) {
            throw new DeleteSectionException(ErrorMessage.CANNOT_REMOVE_ONLY_ONE_SECTION);
        }
        Section lastSection = sections.get(sections.size() - 1);
        if (!lastSection.equalUpStation(station)) {
            throw new DeleteSectionException(ErrorMessage.IS_NOT_LAST_SECTION);
        }
        this.sections.remove(this.sections.size() - 1);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public Long findLastSectionDistance() {
        return this.sections.get(this.sections.size() - 1).getDistance();
    }
}
