package nextstep.subway.domain.utils;

import nextstep.subway.domain.Section;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SectionUtils {
    public static Section getLastSection(List<Section> sections) {
        int sectionLastIndex = sections.size() - 1;
        return sections.get(sectionLastIndex);
    }

    public static List<Long> getIdsIn(List<Section> sections) {
        return sections.stream()
                .map(Section::getStationIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static Long getDownStationIdIn(Section section) {
        return section.getDownStation().getId();
    }



}
