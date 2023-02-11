package subway.line;

import subway.station.Station;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", fetch = LAZY, cascade = PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Station getTerminalStation() {
        validateSections();

        final Section lasSection = sections.get(sections.size() - 1);
        return lasSection.getDownStation();
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
}
