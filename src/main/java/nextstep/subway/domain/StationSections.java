package nextstep.subway.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

public class StationSections {

    @OneToMany(mappedBy = "stationLine", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<StationSection> stationSections;

    public StationSections(List<StationSection> stationSections) {
        this.stationSections = stationSections;
    }

    public StationSection findLastSection() {
        return stationSections.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 노선에는 구간이 존재하지 않습니다."));
    }

    private boolean isExistStation(StationSection stationSection) {
        return stationSections.stream()
                .anyMatch(section -> findStation(section, stationSection));
    }

    private boolean findStation(StationSection section, StationSection stationSection) {
        return section.getUpStationId().equals(stationSection.getDownStationId()) ||
                section.getDownStationId().equals(stationSection.getDownStationId());
    }

    public void addSection(StationLine stationLine, StationSection stationSection) {
        validateSection(stationSection);
        stationSections.add(stationSection);
        stationSection.setStationLine(stationLine);
    }

    private void validateSection(StationSection stationSection) {
        if (isExistStation(stationSection)) {
            throw new IllegalArgumentException("해당 하행역은 이미 노선에 존재합니다.");
        }

        if (isSameLastUpStationAndDownStation(stationSection)) {
            throw new IllegalArgumentException("해당 하행역은 마지막 구간의 상행역이 아닙니다.");
        }
    }

    private boolean isSameLastUpStationAndDownStation(StationSection stationSection) {
        return getLastSection().isSameUpStationAndDownStation(stationSection);
    }

    private StationSection getLastSection() {
        return stationSections.get(getLastIndex());
    }

    private int getLastIndex() {
        return stationSections.size() - 1;
    }
}
