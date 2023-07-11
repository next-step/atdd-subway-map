package subway.line.domain;

import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import subway.common.exception.ErrorMessage;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

import subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public void addSection(Section section) {
        if (notValidSection(section) && alreadyValidSection(section)) {
           throw new IllegalArgumentException(ErrorMessage.SECTION_INTEGRITY_ADD_ERROR_MESSAGE);
        }
        sections.add(section);
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        setLastStations(stations);

        return stations;
    }

    public void deleteSection(Station station) {
        if(isNotLastStation(station) || sections.size() < 2) {
            throw new IllegalArgumentException(ErrorMessage.SECTION_INTEGRITY_REMOVE_ERROR_MESSAGE);
        }
        sections.remove(sections.size() - 1);
    }

    private boolean notValidSection(Section section) {
        if (sections.size() > 0) {
            Station endStation = sections.get(sections.size() - 1).getDownStation();
            return !endStation.equals(section.getUpStation());
        }
        return false;
    }

    private boolean alreadyValidSection(Section section) {
        return sections.stream()
                .map(Section::getDownStation)
                .anyMatch(station -> station.getId().equals(section.getDownStation().getId()));
    }

    private void setLastStations(List<Station> stations) {
        getLastSection().ifPresent(section -> stations.add(section.getDownStation()));
    }

    private Optional<Section> getLastSection() {
        if (sections.size() > 0) {
            return Optional.of(sections.get(sections.size() - 1));
        }
        return Optional.empty();
    }

    private boolean isNotLastStation(Station station) {

        return getLastSection()
                .filter(section -> section.getDownStation().getId().equals(station.getId()))
                .isEmpty();
    }
}
