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
        // 상행역이 하행종점역이 아닌지
        if (!isDownTerminal(newSection.getUpStation())) {
            throw new IllegalArgumentException(String.format("상행역은 하행종점역이어야 합니다. Station: %s", newSection.getUpStation()));
        }
        // 이미 등록된 하행역인지
        if (isContainsStation(newSection.getDownStation())) {
            throw new IllegalArgumentException(String.format("이미 등록된 역입니다. Station: %s", newSection.getDownStation()));
        }
    }

    private boolean isContainsStation(Station downStation) {
        return getAllStations().contains(downStation);
    }

    private boolean isDownTerminal(Station station) {
        return station.equals(getDownTerminal());
    }

    private Station getDownTerminal() {
        return sections.get(sections.size() - 1).getDownStation();
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
        // 마지막 구간인지 아닌지
        Section section = getLastSection();
        if (!section.getDownStation().equals(station)) {
            throw new IllegalArgumentException(String.format("마지막 구간이 아닙니다. Section: %s", section));
        }
    }
}