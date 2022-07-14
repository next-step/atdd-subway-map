package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class StationSections {

    private static final int SECTION_MIN_COUNT = 1;

    @OneToMany(mappedBy = "stationLine", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<StationSection> stationSections = new ArrayList<>();

    public StationSections() {
    }

    public void addSection(StationLine stationLine, StationSection stationSection) {
        validateSection(stationSection);
        stationSections.add(stationSection);
        stationSection.setStationLine(stationLine);
    }

    public List<Long> getStationIds() {
        List<Long> stationIds = new ArrayList<>();
        stationSections.forEach(
                section -> {
                    stationIds.add(section.getDownStationId());
                    stationIds.add(section.getUpStationId());
                }
        );
        return stationIds.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean isExistStation(StationSection stationSection) {
        return stationSections.stream()
                .anyMatch(section -> findStation(section, stationSection));
    }

    private boolean findStation(StationSection section, StationSection stationSection) {
        return section.getUpStationId().equals(stationSection.getDownStationId()) ||
                section.getDownStationId().equals(stationSection.getDownStationId());
    }

    private void validateSection(StationSection stationSection) {
        if (isExistStation(stationSection)) {
            throw new IllegalArgumentException("해당 하행역은 이미 노선에 존재합니다.");
        }

        if (isNoneSameLastUpStationAndDownStation(stationSection)) {
            throw new IllegalArgumentException("해당 하행역은 마지막 구간의 상행역이 아닙니다.");
        }
    }

    private boolean isNoneSameLastUpStationAndDownStation(StationSection stationSection) {
        if(stationSections.isEmpty()){
            return false;
        }
        return getLastSection().isNoneSameDownStationId(stationSection.getUpStationId());
    }

    private StationSection getLastSection() {
        return stationSections.get(getLastIndex());
    }

    private int getLastIndex() {
        return stationSections.size() - 1;
    }

    public StationSection findByDownStationId(Long stationId) {
        validateDeleteCondition(stationId);
        return getLastSection();
    }

    private void validateDeleteCondition(Long stationId) {
        if (isNoneLastSectionDownStation(stationId)) {
            throw new IllegalArgumentException("마지막 구간만 제거할 수 있습니다.");
        }
        if (hasMinSize()) {
            throw new IllegalStateException("노선에 구간이 1개일 경우 삭제할 수 없습니다.");
        }
    }

    private boolean hasMinSize() {
        return stationSections.size() <= SECTION_MIN_COUNT;
    }

    private boolean isNoneLastSectionDownStation(Long stationId) {
        return getLastSection().isNoneSameDownStationId(stationId);
    }

    public void delete(StationSection section) {
        stationSections.remove(section);
    }
}
