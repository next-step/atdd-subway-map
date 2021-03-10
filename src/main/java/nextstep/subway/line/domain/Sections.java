package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DeleteStationIsNotLastStationException;
import nextstep.subway.line.exception.NewDownStationIsAlreadyRegistered;
import nextstep.subway.line.exception.NewUpStationIsWrongException;
import nextstep.subway.line.exception.OnlyOneSectionLeftCannotBeDeleted;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Station> getStations() {
        return findAllStations();
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        validateNewUpStationAndCurrentLastStationAreMatched(section);
        validateNewDownStationShouldNotBeAlreadyRegistered(section);

        sections.add(section);
    }

    private void validateNewUpStationAndCurrentLastStationAreMatched(Section section) {
        if (section.getUpStation().equals(findLastStation())) return;
        throw new NewUpStationIsWrongException();
    }

    private Station findLastStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    private void validateNewDownStationShouldNotBeAlreadyRegistered(Section section) {
        if (!findAllStations().contains(section.getDownStation())) return;
        throw new NewDownStationIsAlreadyRegistered();
    }

    private List<Station> findAllStations() {
        return sections.stream().flatMap(it -> it.getStations().stream()).distinct().collect(Collectors.toList());
    }

    public void deleteSection(Station station) {
        validateStationAndCurrentLastStationAreMatched(station);
        validateOnlyOneSectionLeftCannotBeDeleted();

        sections.remove(sections.size() - 1);
    }

    private void validateStationAndCurrentLastStationAreMatched(Station station) {
        if (station.equals(findLastStation())) return;
        throw new DeleteStationIsNotLastStationException();
    }

    private void validateOnlyOneSectionLeftCannotBeDeleted() {
        if (sections.size() == 1) throw new OnlyOneSectionLeftCannotBeDeleted();
    }
}
