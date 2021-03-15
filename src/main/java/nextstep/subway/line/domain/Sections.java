package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidStationIdException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int ONE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(final Section section) {
        sections.add(section);
    }

    public Station getFinalStation() {
       return sections.get(sections.size() - ONE).getDownStation();
    }

    public boolean isValidDownStation(final Station downStation) {
        return !sections.stream()
                .anyMatch( section -> section.equalsWithEitherUpOrDown(downStation) );
    }

    public void deleteSection(final Long stationId) {
        Section lastSection = sections.stream()
                .filter(section -> section.hasAsDownStation(stationId))
                .findAny().orElseThrow(InvalidStationIdException::new);

        this.sections.remove(lastSection);
    }

    public boolean hasOnlyOneSection() {
        return this.sections.size() == ONE;
    }

    public Section get(int index) {
        return sections.get(index);
    }

    public List<? extends Station> fetchAllDownStations() {
        return sections.stream().map(Section::getDownStation).collect(Collectors.toList());
    }
}
