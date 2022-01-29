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
    private static final int FIRST_INDEX = 0;
    private static final int LAST_INDEX_OPERAND = 1;
    private static final int MIN_SECTION_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        validateAddSection(section);
        sections.add(section);
    }

    public void remove(Station station) {
        validateRemoveSection(station);
        sections.remove(lastIndex());
    }

    public List<Station> stationList() {
        List<Station> stationList = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        stationList.add(FIRST_INDEX, firstStation());

        return Collections.unmodifiableList(stationList);
    }

    private void validateAddSection(Section section) {
        if (sections.isEmpty()) {
            return;
        }
        checkUpStationCondition(section.getUpStation());
        checkDownStationCondition(section.getDownStation());
    }

    private void checkUpStationCondition(Station upStation) {
        if (upStation.equals(lastStation())) {
            return;
        }
        throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
    }

    private void checkDownStationCondition(Station downStation) {
        if (sections.stream()
                .anyMatch(section -> section.getDownStation().equals(downStation))
                || firstStation().equals(downStation)) {
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.");
        }
    }

    private Station firstStation() {
        return sections.get(FIRST_INDEX).getUpStation();
    }

    private Station lastStation() {
        return sections.get(lastIndex()).getDownStation();
    }

    private int lastIndex() {
        return sections.size() - LAST_INDEX_OPERAND;
    }

    private void validateRemoveSection(Station station) {
        checkLastDownStationCondition(station);
        checkLastSectionCondition();
    }

    private void checkLastDownStationCondition(Station station) {
        if (!lastStation().equals(station)) {
            throw new IllegalArgumentException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있습니다.");
        }
    }

    private void checkLastSectionCondition() {
        if (sections.size() == MIN_SECTION_SIZE) {
            throw new IllegalArgumentException("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없습니다.");
        }
    }
}
