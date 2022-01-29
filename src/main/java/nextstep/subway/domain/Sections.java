package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int FIRST = 0;
    private static final int GAP_SIZE = 1;
    private static final int AVAILABLE_REMOVE_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(final Section section) {
        validateAddSection(section);
        this.sections.add(section);
    }

    private void validateAddSection(final Section section) {
        if (isAvailableUpStation(section.getUpStation()) || isAvailableDownStation(section.getDownStation())) {
            throw new IllegalArgumentException("invalid add section");
        }
    }

    private boolean isAvailableUpStation(final Station upStation) {
        if (sections.isEmpty()) {
            return false; // 3항 사용 X
        }
        return isNotDownStation(upStation);
    }

    private boolean isAvailableDownStation(final Station downStation) {
        return sections.stream()
                .anyMatch(it -> it.isAnyStation(downStation));
    }

    public void remove(final Station station) {
        validateRemoveSection(station);
        this.sections.remove(lastSection());
    }

    private void validateRemoveSection(final Station station) {
        if (isAvailableRemove() || isNotDownStation(station)) {
            throw new IllegalArgumentException("invalid remove section");
        }
    }

    private boolean isAvailableRemove() {
        return sections.size() <= AVAILABLE_REMOVE_SIZE;
    }

    private boolean isNotDownStation(final Station upStation) {
        final Section lastSection = lastSection(); // 디미터 법칙 고려
        return !lastSection.isDownStation(upStation);
    }

    private Section lastSection() {
        return sections.get(sections.size() - GAP_SIZE);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }
        final List<Station> stations = sections.stream()
                .map(it -> it.getDownStation())
                .collect(Collectors.toList());
        final Section firstSection = sections.get(FIRST);
        final Station upEndStation = firstSection.getUpStation();
        stations.add(upEndStation);
        return Collections.unmodifiableList(stations);
    }
}
