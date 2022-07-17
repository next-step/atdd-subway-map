package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.exception.SectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {
    private final static int MINIMUM_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private final List<Section> sections = new ArrayList<>();

    public void add(Section newSection) {
        if (!sections.isEmpty()) {
            validateAddSection(newSection);
        }
        sections.add(newSection);
    }

    private void validateAddSection(Section newSection) {
        if (isNotDownTerminal(newSection.getUpStation())) {
            throw new SectionException(ErrorCode.INVALID_UP_STATION_EXCEPTION);
        }
        if (isContainsStation(newSection.getDownStation())) {
            throw new SectionException(ErrorCode.ALREADY_CONTAINS_STATION_EXCEPTION);
        }
    }

    private boolean isContainsStation(Station downStation) {
        return getAllStations().contains(downStation);
    }

    private boolean isNotDownTerminal(Station station) {
        return !station.equals(getDownTerminal());
    }

    private Station getDownTerminal() {
        return getLastSection().getDownStation();
    }

    public List<Station> getAllStations() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public void delete(Station station) {
        validateDeleteSection(station);
        sections.remove(getLastSection());
    }

    private Section getLastSection() {
        return sections.get(sections.size()-1);
    }

    private void validateDeleteSection(Station station) {
        if (isNotLastSection(station)) {
            throw new SectionException(ErrorCode.DELETE_SECTION_DENIED_EXCEPTION);
        }
        if (sections.size() == MINIMUM_SIZE) {
            throw new SectionException(ErrorCode.INVALID_SIZE_SECTION_EXCEPTION);
        }
    }

    private boolean isNotLastSection(Station station) {
        Section section = getLastSection();
        return !section.getDownStation().equals(station);
    }
}