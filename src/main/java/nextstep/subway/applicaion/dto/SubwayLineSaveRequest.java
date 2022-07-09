package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.SubwayLine;

public class SubwayLineSaveRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SubwayLineSaveRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SubwayLine toEntity() {
        return new SubwayLine(this.name, this.color, this.upStationId, this.downStationId, this.distance);
    }
}
