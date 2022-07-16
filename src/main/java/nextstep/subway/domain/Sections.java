package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
            throw new IllegalArgumentException(String.format("상행역은 하행종점역이어야 합니다. Station: %s", newSection.getUpStation()));
        }
        if (isContainsStation(newSection.getDownStation())) {
            throw new IllegalArgumentException(String.format("이미 등록된 역입니다. Station: %s", newSection.getDownStation()));
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
        Section section = getLastSection();
        if (!section.getDownStation().equals(station)) {
            throw new IllegalArgumentException(String.format("마지막 구간이 아닙니다. Section: %s", section));
        }
        if (sections.size() == MINIMUM_SIZE) {
            throw new IllegalArgumentException("구간이 1개일 때는 구간 제거가 불가능합니다.");
        }
    }
}