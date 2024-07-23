package subway.domain.section;

import subway.common.exception.InsufficientStationsException;
import subway.common.exception.InvalidDownStationException;
import subway.common.exception.InvalidUpSationException;
import subway.common.exception.NotLastStationException;
import subway.common.response.ErrorCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if(this.sections.isEmpty()){
            this.sections.add(section);
            return;
        }
        // 새로운 구간의 상행역이 노선에 등록되어있는 하행 종점역이 아닌 경우
        if(!getLastDownStationId().equals(section.getUpStationId())) {
            throw new InvalidUpSationException(ErrorCode.INVALID_UP_STATION_ADD);
        }
        // 새로운 구간의 하행역이 이미 해당 노선에 등록되어있는 경우
        if(getStationIds().contains(section.getDownStationId())) {
            throw new InvalidDownStationException(ErrorCode.INVALID_DOWN_STATION_ADD);
        }
        this.sections.add(section);

    }

    public Collection<Long> getStationIds() {
        List<Long> stationIds = sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());
        stationIds.add(getLastDownStationId());
        return stationIds;
    }
    public Long getLastDownStationId() {
        return sections.get(sections.size() - 1).getDownStationId();
    }


    public void removeLastStation(Long stationId) {
        // 구간이 1개 이하인경우,
        if (this.sections.size() <= 1) {
            throw new InsufficientStationsException(ErrorCode.INSUFFICIENT_STATION_DELETE);
        }
        // 지하철 노선에 등록된 역(하행 종점역)이 아닌 경우
        if (!getLastDownStationId().equals(stationId)) {
            throw new NotLastStationException(ErrorCode.NOT_LAST_STATION_DELETE);
        }


        this.sections.remove(sections.size() - 1);

    }
}
