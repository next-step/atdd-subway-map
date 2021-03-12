package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade ={CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @Transient
    private final int MINIMUM_SECTION_SIZE = 1;

    public void add(Section section) {
        if (isAlreadyRegistered(section) || !isAppendableSection(section) ) {
            throw new InvalidSectionException("Input section is invalid");
        }
        this.sections.add(section);
    }

    private Boolean isLeftOneSection(){
        return (this.sections.size() == MINIMUM_SECTION_SIZE);
    }

    private Boolean isEmptySections() {
        return (this.sections.size() < MINIMUM_SECTION_SIZE);
    }

    private Boolean isAppendableSection(final Section section) {
        if (isEmptySections()) {
            return true;
        }
        return (getLastSection().getDownStation().getId() == section.getUpStation().getId());
    }

    private Boolean isAlreadyRegistered(final Section section) {
        return sections.stream()
                .anyMatch(s -> s.getUpStation().equals(section.getDownStation())
                        && s.getDownStation().equals(section.getDownStation()));
    }

    public void delete(final Station station){
        final Section section = getLastSection();
        if(!section.getDownStation().equals(station) || isLeftOneSection()){
            throw new InvalidSectionException("Invalid Station Id" + station.getId());
        }
        sections.remove(section);
    }

    public List<Station> getStations(){
        List<Station> sequentialStations = new ArrayList<>();
        for (Station station = firstStation(); station != null; station = nextStation(station)){
            sequentialStations.add(station);
        }
        return sequentialStations.stream()
                .collect(Collectors.toList());
    }

    private Station firstStation(){
        return sections.stream()
                .filter(section -> !matchAnyDownStation(section.getUpStation()))
                .map(section -> section.getUpStation())
                .findFirst()
                .orElse(null);
    }

    private Station nextStation(final Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .map(section -> section.getDownStation())
                .findFirst()
                .orElse(null);
    }

    public Section getLastSection(){
        return sections.stream()
                .filter(section -> !matchAnyUpStation(section.getDownStation()))
                .findFirst()
                .orElse(null);
    }

    private Boolean matchAnyUpStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.getUpStation().equals(station));
    }

    private Boolean matchAnyDownStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.getDownStation().equals(station));
    }

    public Integer getLineDistance() {
        return sections.stream().
                reduce(0, (totalDistance, section) -> totalDistance + section.getDistance(), Integer::sum);
    }
}
