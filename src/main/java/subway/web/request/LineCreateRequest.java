package subway.web.request;

import subway.domain.LineCreateDomain;

public class LineCreateRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public LineCreateRequest() {
    }

    public LineCreateRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public LineCreateDomain toDomain() {
        return new LineCreateDomain(name, color, upStationId, downStationId, distance);
    }

}
