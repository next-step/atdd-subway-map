package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class LineSections {
    private final List<Section> sections;

    public LineSections(List<Section> sections) {
        this.sections = sections;
    }

    public Section getUpEndpoint() {
        List<Long> upStationIds = getUpStationIds();
        List<Long> downStationIds = getDownStationIds();
        upStationIds.removeAll(downStationIds);
        Long upStationId = upStationIds.get(0);
        return sections.stream()
                .filter(v -> v.getUpStation().getId().equals(upStationId))
                .collect(Collectors.toList())
                .get(0);
    }

    public Section getDownEndpoint() {
        List<Long> upStationIds = getUpStationIds();
        List<Long> downStationIds = getDownStationIds();
        downStationIds.removeAll(upStationIds);
        Long downStationId = downStationIds.get(0);
        return sections.stream()
                .filter(v -> v.getDownStation().getId().equals(downStationId))
                .collect(Collectors.toList())
                .get(0);
    }

    private List<Long> getDownStationIds() {
        return sections.stream().map(v -> v.getDownStation().getId()).collect(Collectors.toList());
    }

    private List<Long> getUpStationIds() {
        return sections.stream().map(v -> v.getUpStation().getId()).collect(Collectors.toList());
    }
}
