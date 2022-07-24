package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.common.exception.CustomException;
import nextstep.subway.common.exception.code.LineCode;
import nextstep.subway.common.exception.code.SectionCode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Sections {
    public static final int FIRST_STATION_IDX = 0;
    public static final int INVALID_REMOVE_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(final Section section) {
        if (!sections.isEmpty()) {
            checkSectionMatch(section);
            checkStationDuplicate(section);
        }
        sections.add(section);
    }

    private void checkSectionMatch(final Section section) {
        if (!getDownEndStation().equals(section.getUpStation())) {
            throw new CustomException(SectionCode.SECTION_NOT_MATCH);
        }
    }

    private void checkStationDuplicate(final Section section) {
        if (isContain(section.getDownStation())) {
            throw new CustomException(LineCode.LINE_STATION_DUPLICATE);
        }
    }

    public boolean isContain(Station station){
        Set<Station> stations = new HashSet<>(getStations());
        return stations.contains(station);
    }

    public Section removeLastSection(final Long stationId) {
        checkInvalidRemoveSize();
        checkIsDownEndStation(stationId, getDownEndStation());
        return sections.remove(size() - 1);
    }

    private void checkInvalidRemoveSize() {
        if (size() <= INVALID_REMOVE_SIZE) {
            throw new CustomException(SectionCode.SECTION_REMOVE_INVALID);
        }
    }

    private void checkIsDownEndStation(final Long stationId, final Station downEndStation) {
        if (!downEndStation.getId().equals(stationId)) {
            throw new CustomException(SectionCode.SECTION_REMOVE_INVALID);
        }
    }

    public int size() {
        return sections.size();
    }

    public List<Station> getStations() {
        return sections.stream()
                       .map(Section::getAllStation)
                       .flatMap(List::stream)
                       .distinct()
                       .collect(Collectors.toUnmodifiableList());
    }

    public Station getDownEndStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }
}
