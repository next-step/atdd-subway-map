package subway.entity.group;

import java.util.Collection;
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
}
