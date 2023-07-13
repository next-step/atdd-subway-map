package subway.subway.domain;

import java.util.*;
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
    void add(SubwaySection station) {
        subwaySections.put(station.getUpStationId(), station);
    }

    public int size() {
        return subwaySections.size();
    }

    boolean isEmpty() {
        return subwaySections.isEmpty();
    }

    private SubwaySection getSection(Station.Id stationId) {
        SubwaySection subwaySection = subwaySections.get(stationId);
        return Objects.requireNonNull(subwaySection, String.format("%d 역은 현재 노선에 존재하지 않은 역입니다.", stationId.getValue()));
    }

    boolean existsUpStation(Station.Id stationId) {
        return subwaySections.containsKey(stationId);
    }

    void validate(Station.Id startStationId) {
        if (isEmpty()) {
            throw new IllegalArgumentException("구간이 비어있습니다.");
        }
        if (!isConnected(startStationId)) {
            throw new IllegalArgumentException("구간이 연결되어있지 않습니다.");
        }
        if (isDuplicated()) {
            throw new IllegalArgumentException("구간이 중복되어있습니다.");
        }
        if (isCircular(startStationId)) {
            throw new IllegalArgumentException("구간이 순환되어있습니다.");
        }
    }

    boolean isCircular(Station.Id startStationId) {
        return subwaySections
                .values()
                .stream()
                .map(SubwaySection::getDownStationId)
                .anyMatch(downStationId -> downStationId.equals(startStationId));
    }

    boolean isDuplicated() {
        return subwaySections
                .values()
                .stream()
                .map(SubwaySection::getDownStationId)
                .distinct()
                .count() != this.size();
    }

    boolean isConnected(Station.Id startStationId) {
        Station.Id stationId = startStationId;
        int count = 0;
        while (count < size() && existsUpStation(stationId)) {
            stationId = getSection(stationId).getDownStationId();
            count++;
        }
        return count == this.size();
    }

    void update(SubwaySection subwaySection, SectionOperateManager manager) {
        SectionOperator operator = manager.getOperator(this);
        operator.apply(this, subwaySection);
    }

    public List<SubwaySection> getSections() {
        return new ArrayList<>(subwaySections.values());
    }
}
