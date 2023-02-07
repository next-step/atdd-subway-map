package subway.section.domain;

import subway.exception.CannotDeleteSectionException;
import subway.exception.SectionNotFoundException;
import subway.station.domain.Station;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    public static final int MIN_SECTION_SIZE_OF_LINE = 1;
    @ElementCollection
    @CollectionTable(name = "section", joinColumns = @JoinColumn(name = "line_id"))
    private List<Section> sections = new LinkedList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void addSection(Section section) {
        // insert last
        sections.add(sections.size(), section);
    }

    public boolean contains(Station downStation) {
        return sections.stream()
                .anyMatch(section -> section.hasStation(downStation));
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getAllStations() {
        return sections.stream()
                .map(section -> List.of(section.getUpStation(), section.getDownStation()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Station getUpStation() {
        return sections.stream()
                .findFirst()
                .orElseThrow(SectionNotFoundException::new)
                .getUpStation();
    }

    public Station getDownStation() {
        Section lastSection = getLastSection();
        return lastSection.getDownStation();
    }

    private Section getLastSection() {
        int size = sections.size();
        if (size < 1) {
            throw new SectionNotFoundException();
        }
        return sections.get(size - 1);
    }

    public void deleteSection(Station deleteStation) {
        if (sections.size() == MIN_SECTION_SIZE_OF_LINE) {
            throw new CannotDeleteSectionException("Line has only 1 section");
        }
        if (!getLastSection().isDownStation(deleteStation)) {
            throw new CannotDeleteSectionException("Station that trying to delete is not downStation of this line.");
        }
        sections.remove(getLastSection());
    }
}
