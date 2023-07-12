package subway.subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SubwaySections {

    private final Map<Station.Id, SubwaySection> subwaySections;

    public SubwaySections(SubwaySection subwaySection) {
        subwaySections = new HashMap<>();
        subwaySections.put(subwaySection.getUpStationId(), subwaySection);
    }

    public SubwaySections(List<SubwaySection> subwaySectionList) {
        subwaySections =
                subwaySectionList
                        .stream()
                        .collect(Collectors.toMap(SubwaySection::getUpStationId, Function.identity()));
    }

    public void add(SubwaySection station) {
        subwaySections.put(station.getUpStationId(), station);
    }

    public int size() {
        return subwaySections.size();
    }

    public boolean contains(SubwaySection subwaySection) {
        return subwaySections.get(subwaySection.getUpStationId()).equals(subwaySection);
    }

    public boolean isEmpty() {
        return subwaySections.isEmpty();
    }

    public SubwaySection getSection(Station.Id stationId) {
        return subwaySections.get(stationId);
    }

    public void validate() {
        if (isEmpty()) {
            throw new IllegalArgumentException("구간이 비어있습니다.");
        }
        if (isConnected()) {
            throw new IllegalArgumentException("구간이 연결되어있지 않습니다.");
        }
        if(!isDuplicated()) {
            throw new IllegalArgumentException("구간이 중복되어있습니다.");
        }
        if(!isCircular()) {
            throw new IllegalArgumentException("구간이 순환되어있습니다.");
        }
    }

    private boolean isCircular(Station.Id startStationId) {
        return subwaySections
                .keySet()
                .stream()
                .map(key -> getSection(key).getDownStationId())
                .anyMatch(downStationId -> downStationId.equals(startStationId));
    }

    /**
     *
     * @return
     */
    private boolean isDuplicated() {
        return false;
    }

    private boolean isConnected() {
        return true;
    }
}
