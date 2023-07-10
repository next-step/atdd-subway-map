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
}
