package nextstep.subway.domain;

import nextstep.subway.domain.exception.SectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();


    public void add(Section section) {
        if (!isCurrentDownStation(section.getUpStation())) {
            throw SectionException.ofIllegalUpStation(section);
        }

        if (isComposedOf(section.getDownStation())) {
            throw SectionException.ofIllegalDownStation(section);
        }

        sections.add(section);
    }

    public void removeStation(Station station) {
        if (!isCurrentDownStation(station)) {
            throw SectionException.ofIllegalDownStation(station);
        }

        if (hasOnlyOneSection()) {
            throw SectionException.ofSectionSize();
        }

        Section targetSection = sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst()
                .orElseThrow(() -> SectionException.ofIllegalDownStation(station));
        sections.remove(targetSection);
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(getCurrentDownStation());
        return stations.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Station getCurrentDownStation() {
        return sections.isEmpty() ? null : sections.get(sections.size() - 1).getDownStation();
    }

    private boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

    private boolean isCurrentDownStation(Station station) {
        return station.equals(getCurrentDownStation());
    }

    private boolean isComposedOf(Station station) {
        return sections.stream().anyMatch(section -> section.isStartWith(station) || section.isEndWith(station));
    }
}
