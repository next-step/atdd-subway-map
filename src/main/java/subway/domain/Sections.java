package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {

    private static final String ADD_ERROR_MESSAGE = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.";
    private static final String ADD_DUPLICATE_ERROR_MESSAGE = "새로운 구간의 하행역이 해당 노선에 등록되어있으면 안됩니다.";
    private static final String NOT_EQUALS_DOWN_END_SECTION_ERROR_MESSAGE = "하행 종점역 구간만 삭제 가능합니다.";
    private static final String ILLEGAL_SIZE_DELETE_SECTION_ERROR_MESSAGE = "구간이 하나인 경우에는 삭제 불가능 합니다.";

    @OneToMany(mappedBy = "lineId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validateNewSection(section);
        sections.add(section);
    }

    public Set<Station> getStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toSet());
    }

    public void delete(Long stationId) {
        if (sections.size() < 2) {
            throw new IllegalArgumentException(ILLEGAL_SIZE_DELETE_SECTION_ERROR_MESSAGE);
        }

        Section downEndSection = sections.get(sections.size() - 1);
        if (!downEndSection.getDownStation().matchId(stationId)) {
            throw new IllegalArgumentException(NOT_EQUALS_DOWN_END_SECTION_ERROR_MESSAGE);
        }

        sections.remove(downEndSection);
    }

    private Station getDownEndStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    private void validateNewSection(Section section) {
        if (!getDownEndStation().equals(section.getUpStation())) {
            throw new IllegalArgumentException(ADD_ERROR_MESSAGE);
        }
        if (getStations().contains(section.getDownStation())) {
            throw new IllegalArgumentException(ADD_DUPLICATE_ERROR_MESSAGE);
        }
    }
}
