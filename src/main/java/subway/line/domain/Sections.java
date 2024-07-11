package subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import subway.Station;

@Embeddable
public class Sections {
    public static final String ALREADY_ADDED_SECTION_MESSAGE = "이미 추가된 구간입니다.";
    public static final String INVALID_UP_STATION_MESSAGE = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.";
    public static final String DUPLICATE_DOWN_STATION_MESSAGE = "이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.";
    public static final String CANNOT_REMOVE_SECTION_MESSAGE = "지하철 노선에 등록된 하행 종점역만 제거할 수 있습니다.";
    public static final String LAST_SECTION_CANNOT_BE_REMOVED_MESSAGE = "지하철 노선에 상행 종점역과 하행 종점역만 있는 경우 역을 삭제할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addSection(Line line, Section section) {
        if (contains(section)) {
            throw new IllegalArgumentException(ALREADY_ADDED_SECTION_MESSAGE);
        }

        if (!isValidUpStation(section.getUpStation())) {
            throw new IllegalArgumentException(INVALID_UP_STATION_MESSAGE);
        }

        if (isStationAlreadyAdded(section.getDownStation())) {
            throw new IllegalArgumentException(DUPLICATE_DOWN_STATION_MESSAGE);
        }

        sections.add(section);
        section.setLine(line);
    }

    private boolean isValidUpStation(Station upStation) {
        return sections.isEmpty() || getLastDownStation().equals(upStation);
    }

    private boolean isStationAlreadyAdded(Station station) {
        return sections.stream()
            .anyMatch(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station));
    }

    private Station getLastDownStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public void removeSection(Station station) {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException(LAST_SECTION_CANNOT_BE_REMOVED_MESSAGE);
        }

        Section sectionToRemove = sections.stream()
            .filter(section -> section.getDownStation().equals(station))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(CANNOT_REMOVE_SECTION_MESSAGE));

        sections.remove(sectionToRemove);
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }

    public List<Section> toUnmodifiableList() {
        return List.copyOf(sections);
    }

    public Section getLastSection() {
        if (sections.isEmpty()) {
            return null;
        }

        return sections.get(sections.size() - 1);
    }
}
