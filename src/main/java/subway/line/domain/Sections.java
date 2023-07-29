package subway.line.domain;

import subway.section.domain.Section;
import subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    Long getOriginSectionId() {
        return getFirst().getUpStationId();
    }

    Long getTerminalSectionId() {
        return getLast().getDownStationId();
    }

    public Section getFirst() {
        return sections.get(0);
    }

    public Section getLast() {
        return sections.get(sections.size() - 1);
    }

    public void add(Section station) {
        sections.add(station);
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .toList();
    }

    public Integer getTotalDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public boolean hasLessThanTwoSections() {
        return sections.size() < 2;
    }
}
