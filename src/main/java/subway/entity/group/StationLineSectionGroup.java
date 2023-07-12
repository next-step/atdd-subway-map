package subway.entity.group;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import subway.entity.StationLineSection;

public class StationLineSectionGroup {

    private final List<StationLineSection> sections;

    private StationLineSectionGroup(List<StationLineSection> sections) {
        this.sections = sections;
    }

    public static StationLineSectionGroup of(final List<StationLineSection> sections) {
        return new StationLineSectionGroup(sections);
    }

    public List<Long> getStationsId() {

        return sections.stream()
            .map(StationLineSection::getStationIdList)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    public StationLineSection getEndDownStation() {
        return sections.stream()
            .sorted(Comparator.comparing(StationLineSection::getId).reversed())
            .limit(1)
            .collect(Collectors.toList())
            .get(0);
    }

    public void validateAdd(long upStationId, long downStationId) {

        validAddedSectionUpStation(upStationId);
        validAddedSectionDownStation(downStationId);
    }

    private void validAddedSectionUpStation(long upStationId) {
        if (!isEqualDownEndStation(upStationId)) {
            throw new IllegalArgumentException("추가하고자 하는 구간의 상행역이, 노선의 하행종점역이 아닙니다.");
        }
    }

    private void validAddedSectionDownStation(long downStationId) {
        if (isExistDownEndStation(downStationId)) {
            throw new IllegalArgumentException("추가하고자 하는 구간의 하행역이 이미 구간에 존재합니다.");
        }
    }

    public boolean isEqualDownEndStation(long addUpStationId) {

        return sections.stream()
            .sorted(Comparator.comparing(StationLineSection::getId).reversed())
            .limit(1)
            .anyMatch(
                stationLineSection -> stationLineSection.getDownStationId().equals(addUpStationId)
            );
    }

    public boolean isExistDownEndStation(long downStationId) {
        return sections.stream()
            .anyMatch(
                stationLineSection -> stationLineSection.getDownStationId().equals(downStationId)
            );
    }

    public void validateDelete(long deleteStationId) {

        validateSizeCanBeDeleted();
        validateDeleteStationIsEndStation(deleteStationId);
    }

    private void validateSizeCanBeDeleted() {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException("구간이 1개인 경우 삭제할 수 없습니다.");
        }
    }

    private void validateDeleteStationIsEndStation(long deleteStationId) {
        if (!getEndDownStation().isEqualsDownStation(deleteStationId)) {
            throw new IllegalArgumentException("하행 종점역이 아니면 삭제할 수 없습니다.");
        }
    }
}
