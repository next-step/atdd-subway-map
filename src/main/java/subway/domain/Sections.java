package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    int getSize() {
        return this.sections.size();
    }

    boolean isLastDownStation(final Station station) {
        return station.equals(getLastDownStation());
    }

    boolean isExistsStationInLine(final Station station) {
        return getStations().contains(station);
    }

    void init(final Line line, final Station upStation, final Station downStation, final int distance) {
        final Section section = new Section(line, upStation, downStation, distance);
        this.sections.add(section);
    }

    void addSection(final SectionValidator sectionValidator, final Line line, final Station upStation, final Station downStation, final int distance) {
        sectionValidator.validateSectionBeforeAdd(this, upStation, downStation);
        final Section section = new Section(line, upStation, downStation, distance);
        this.sections.add(section);
    }

    List<Station> getStations() {
        final Station lastDownStation = getLastDownStation();
        final List<Station> stations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        stations.add(lastDownStation);
        return stations;
    }

    void removeSection(final SectionValidator sectionValidator, final Station station) {
        sectionValidator.validateBeforeRemoveStation(this, station);
        final Section lastSection = this.sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst().get();
        this.sections.remove(lastSection);
    }

    private Station getLastDownStation() {
        final Station downStation = this.sections.stream().findFirst().get().getDownStation();
        return getLastDownStation(downStation);
    }

    private Station getLastDownStation(final Station station) {
        final Optional<Section> findSection = this.sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findAny();
        if (findSection.isEmpty()) {
            return station;
        }
        ;
        return getLastDownStation(findSection.get().getDownStation());
    }
}
