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

    public Section getLastSection() {
        return values.stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new IllegalArgumentException("노선에 구간이 존재하지 않습니다."));
    }

    public void addSection(Section section) {
        validateLastStation(section);
        validateDuplicateStation(section);
        values.add(section);
    }

    private void validateLastStation(Section section) {
        Long lastStationId = getLastSection().getDownStationId();
        if (!lastStationId.equals(section.getUpStationId())) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 노선의 하행 종착역과 같아야 합니다. upStationId: " + section.getUpStationId());
        }
    }

    private void validateDuplicateStation(Section section) {
        Long downStationId = section.getDownStationId();
        if (values.stream().anyMatch(value -> value.containStationId(downStationId))) {
            throw new IllegalArgumentException("주어진 하행역은 이미 노선에 등록되어 있는 등록된 역입니다. downStationId: " + downStationId);
        }
    }
}
