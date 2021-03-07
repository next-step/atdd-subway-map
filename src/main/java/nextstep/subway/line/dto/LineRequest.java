package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() { return upStationId; }

    public Long getDownStationId() { return downStationId; }

    public int getDistance() { return distance; }

    public Line toLine() {
        return new Line(name, color);
    }

    public Section toSection() {
        // NOTE: Station 생성 시에 Name 만 초기값인데, 실제 요청에서는 Long으로 Id값만 받고 있다.
        return new Section(new Station(upStationId.toString()), new Station(downStationId.toString()), distance);
    }
}
