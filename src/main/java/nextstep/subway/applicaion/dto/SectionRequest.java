package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class SectionRequest {

    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SectionRequest(){

    }

    public SectionRequest(LineRequest lineRequest) {
        this.upStationId = lineRequest.getUpStationId();
        this.downStationId = lineRequest.getDownStationId();
        this.distance = lineRequest.getDistance();
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
