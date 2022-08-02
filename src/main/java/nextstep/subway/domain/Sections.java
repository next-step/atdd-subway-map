package nextstep.subway.domain;

import nextstep.subway.exceptions.NotMatchedSectionRuleException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final String EQUAL_CURRENT_DOWN_STATION_WITH_NEW_UP_STATION
            = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역과 같아야 합니다.";

    private static final String NOT_EXIST_NEW_DOWN_STATION
            = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public List<Section> value() {
        return sections;
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public Section get(int index) {
        return this.sections.get(index);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        for (Section section : this.sections) {
            stations.addAll(
                    section.getStations()
                        .stream()
                        .filter(station -> !stations.contains(station))
                        .collect(Collectors.toList()));
        }
        return stations;
    }

    public void checkSectionRulesOrThrow(Long upStationId, Long downStationId) {
        equalCurrentDownStationIdWithNewUpStationIdOrThrow(upStationId);
        notExistDownStationIdOrThrow(downStationId);
    }

    public void equalCurrentDownStationIdWithNewUpStationIdOrThrow(Long upStationId) {
        if(!(this.sections.get(this.sections.size() - 1).getDownStationId() == upStationId)) {
            throw new NotMatchedSectionRuleException(EQUAL_CURRENT_DOWN_STATION_WITH_NEW_UP_STATION);
        }
    }

    public void notExistDownStationIdOrThrow(Long downStationId) {
        if(this.getStations().stream().anyMatch(station -> station.getId() == downStationId)) {
            throw new NotMatchedSectionRuleException(NOT_EXIST_NEW_DOWN_STATION);
        }
    }

    public Section getLastSection(Long stationId) {
        return this.sections.get(this.sections.size() - 1);
    }

    public void removeSection() {
        this.sections.remove(this.sections.size() - 1);
    }
}
