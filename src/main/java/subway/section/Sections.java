package subway.section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> values;

    public Sections(List<Section> values) {
        this.values = values;
    }

    public List<Long> getStationIds() {
        Long firstStationId = getFirstStationId();
        List<Long> downStationIds = getDownStationIds();
        List<Long> stationIds = new ArrayList<>();
        stationIds.add(firstStationId);
        stationIds.addAll(downStationIds);
        return stationIds;
    }

    private Long getFirstStationId() {
        return values.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("노선에 구간이 존재하지 않습니다."))
                .getUpStationId();
    }

    private List<Long> getDownStationIds() {
        return values.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toList());
    }
}
