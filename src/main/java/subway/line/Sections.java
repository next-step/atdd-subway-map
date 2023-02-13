package subway.line;

import subway.common.exception.line.CannotRemoveNotTerminalStation;
import subway.common.exception.line.LineThatHasOnlyOneSectionCannotRemoveStationException;
import subway.station.Station;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", fetch = LAZY, cascade = {PERSIST, MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Station getTerminalStation() {
        return getLastSection().getDownStation();
    }

    private Section getLastSection() {
        validateSections();
        return sections.get(sections.size() - 1);
    }

    public boolean hasStation(final Station station) {
        return getStations().stream()
                    .anyMatch(s -> s == station);
    }

    public List<Station> getStations() {
        final List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        stations.add(getTerminalStation());

        return stations;
    }

    private void validateSections() {
        if (sections.isEmpty()) {
            throw new AssertionError("노선은 구간이 없을 수가 없습니다!!");
        }
    }

    public void add(final Section section) {
        this.sections.add(section);
    }

    public void remove(Station station) {
        validateRemoveSection(station);
        this.sections.remove(getLastSection());
    }

    private void validateRemoveSection(Station station) {
        if (isNotTerminalStation(station)) {
            throw new CannotRemoveNotTerminalStation();
        }

        if (isOnlyOneSection()) {
            throw new LineThatHasOnlyOneSectionCannotRemoveStationException();
        }
    }

    private boolean isOnlyOneSection() {
        return sections.size() < 2;
    }

    private boolean isNotTerminalStation(Station station) {
        return getTerminalStation() != station;
    }
}
