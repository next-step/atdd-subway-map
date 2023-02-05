package subway.section.domain;

import lombok.Getter;
import subway.section.exception.NotLastSectionException;
import subway.section.exception.SectionNotFoundException;
import subway.section.exception.SingleSectionException;
import subway.station.domain.Station;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Embeddable
@Getter
public class Sections {

    @OneToMany(mappedBy = "line", cascade = ALL, orphanRemoval = true)
    private final List<Section> sectionList = new ArrayList<>();

    public Set<Station> getStations() {
        Set<Station> stations = new HashSet<>();

        sectionList.forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });

        return stations;
    }

    public void addSection(final Section section) {
        sectionList.add(section);
    }

    public void remove(final Station station) {
        Section lastSection = getLastSection();

        if (!lastSection.getDownStation().equals(station)) {
            throw new NotLastSectionException();
        }

        sectionList.remove(lastSection);
    }

    private Section getLastSection() {
        if (sectionList.isEmpty()) {
            throw new SectionNotFoundException();
        }

        if (sectionList.size() < 2) {
            throw new SingleSectionException();
        }

        return sectionList.get(sectionList.size() - 1);
    }

    public boolean isNotExistDownStation(Station station) {
        return sectionList.stream()
                .map(Section::getDownStation)
                .noneMatch(downStation -> downStation.equals(station));
    }

    public boolean isAlreadyRegisteredStation(final Station station) {
        return sectionList.stream()
                .anyMatch(section -> section.getUpStation().equals(station)
                        || section.getDownStation().equals(station));
    }
}
