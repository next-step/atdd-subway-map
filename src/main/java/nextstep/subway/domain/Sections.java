package nextstep.subway.domain;

import nextstep.subway.exceptions.NotMatchedSectionCreateRuleException;
import nextstep.subway.exceptions.NotMatchedSectionDeleteRuleException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    public static final String EQUAL_CURRENT_DOWN_STATION_WITH_NEW_UP_STATION
            = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역과 같아야 합니다.";

    public static final String NOT_EXIST_NEW_DOWN_STATION
            = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.";

    public static final String ABLE_TO_DELETE_ONLY_LAST_SECTION = "하행 종점역이 포함된 구간만 제거할 수 있습니다.";

    public static final String DELETE_WHEN_SECTION_TWO_OR_MORE
            = "지하철 구간의 개수가 2 이상일 때만 구간을 제거할 수 있습니다.";

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
                    section.getStationsToBe()
                        .stream()
                        .filter(station -> !stations.contains(station))
                        .collect(Collectors.toList()));
        }
        return stations;
    }

    public void checkSectionCreateRulesOrThrow(Long upStationId, Long downStationId) {
        equalCurrentDownStationIdWithNewUpStationIdOrThrow(upStationId);
        notExistDownStationIdOrThrow(downStationId);
    }

    public void equalCurrentDownStationIdWithNewUpStationIdOrThrow(Long upStationId) {
        if(!(this.sections.get(this.sections.size() - 1).getDownStationId() == upStationId)) {
            throw new NotMatchedSectionCreateRuleException(EQUAL_CURRENT_DOWN_STATION_WITH_NEW_UP_STATION);
        }
    }

    public void notExistDownStationIdOrThrow(Long downStationId) {
        if(this.getStations().stream().anyMatch(station -> station.getId() == downStationId)) {
            throw new NotMatchedSectionCreateRuleException(NOT_EXIST_NEW_DOWN_STATION);
        }
    }

    public void removeSection(Long stationId) {
        checkSectionDeleteRulesOrThrow(stationId);

        int lastSectionIndex = this.sections.size() - 1;
        this.sections.get(lastSectionIndex).delete();
        this.sections.remove(lastSectionIndex);
    }

    public void checkSectionDeleteRulesOrThrow(Long stationId) {
        if (this.sections.size() < 2) {
            throw new NotMatchedSectionDeleteRuleException(DELETE_WHEN_SECTION_TWO_OR_MORE);
        }

        if (!equalDownStationIdWithRequestStationId(stationId)) {
            throw new NotMatchedSectionDeleteRuleException(ABLE_TO_DELETE_ONLY_LAST_SECTION);
        }
    }

    private boolean equalDownStationIdWithRequestStationId(Long stationId) {
        return this.sections.get(this.sections.size() - 1).getDownStationId() == stationId;
    }
}
