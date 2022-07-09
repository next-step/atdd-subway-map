package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;

import java.util.List;

public class StationLineSaveRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public StationLineSaveRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationLine toEntity() {
        return new StationLine(this.name, this.color, this.upStationId, this.downStationId, this.distance);
    }
}
