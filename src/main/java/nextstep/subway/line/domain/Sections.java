package nextstep.subway.line.domain;

import nextstep.subway.line.exception.NewDownStationIsAlreadyRegistered;
import nextstep.subway.line.exception.NewUpStationIsWrongException;
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

    private Set<Station> findAllStations() {
        return sections.stream().flatMap(it -> it.getStations().stream()).collect(Collectors.toSet());
    }
}
