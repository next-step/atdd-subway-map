package subway.section.domain;

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
                .filter(section -> section.hasStation(downStation))
                .findFirst()
                .isPresent();
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
        int size = sections.size();
        if (size < 1) {
            throw new SectionNotFoundException();
        }
        Section lastSection = sections.get(size - 1);
        return lastSection.getDownStation();
    }
}
