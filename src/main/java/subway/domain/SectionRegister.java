package subway.domain;

import java.util.Set;

public class SectionRegister {

    public void registSectionInLine(Set<Station> stationsInLine, Line line, Section newSection) {
        checkStationsInLine(stationsInLine, line);
        validateNewSectionDownStationNotInExistingInStations(stationsInLine, newSection);
        updateLineIdForMatchedStations(stationsInLine, line, newSection);
        line.registSection(newSection);
    }

    private static void checkStationsInLine(Set<Station> stations, Line line) {
        if (stations.stream().anyMatch(station -> {
            // 라인에 등록된 지하철역인지 확인
            return !station.isStationInLine(line.getId());
        })) {
            throw new IllegalArgumentException("노선에 등록되지 않은 역이 포함되어 있습니다.");
        }
    }

    private static void validateNewSectionDownStationNotInExistingInStations(Set<Station> stations,
                                                                             Section newSection) {
        if (stations.stream()
                .anyMatch(station -> station.equalsId(newSection.getDownStationId()))) {
            throw new IllegalArgumentException("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
        }
    }

    private void updateLineIdForMatchedStations(Set<Station> stationsInLine, Line line,
                                                Section newSection) {
        stationsInLine.stream()
                .filter(station -> station.equalsId(newSection.getUpStationId()) || station.equalsId(
                        newSection.getDownStationId()))
                .forEach(station -> station.updateLineId(line.getId()));
    }
}
