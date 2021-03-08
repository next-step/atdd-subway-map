package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionRequest(){
    }

    public SectionRequest(final Long upStationId, final Long downStationId, final int distance){
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section toSection(){
        return new Section(new Station(this.upStationId.toString()), new Station(this.downStationId.toString()), this.distance);
    }
}
