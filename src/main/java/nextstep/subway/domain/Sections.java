package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public List<Section> getAllSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public List<Station> getSectionStations(Function<Section, Station> func) {
        return sections
            .stream()
            .map(func)
            .collect(Collectors.toList());
    }

    public Station getLastDownStation() {
        return sections
            .stream()
            .map(Section::getDownStation)
            .reduce((a, b) -> b)
            .orElseThrow(() -> new NoSuchElementException("하행 종점역이 없습니다."));
    }

    public boolean isAvailableDelete() {
        return sections.size() > 1;
    }

    public Section getByDownStation(Station station) {
        return sections.stream()
            .filter(section -> section.getDownStation() == station)
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("일치하는 구간을 찾을 수 없습니다."));
    }

    public List<Station> getAllStations() {
        List<Station> allStations = sections.
            stream()
            .map(Section::getUpStation)
            .collect(Collectors.toList());

        allStations.add(getLastDownStation());

        return allStations;
    }
}
