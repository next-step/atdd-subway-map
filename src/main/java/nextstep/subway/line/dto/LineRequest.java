package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;       // 추가
    private Long downStationId;     // 추가
    private int distance;           // 추가

    public LineRequest() {
    }

    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
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

    public int getDistance() {
        return distance;
    }

    public Line toLine() {
        return new Line(name, color);
    }

    public LineRequest name(String name) {
        this.name = name;
        return this;
    }

    public LineRequest color(String color) {
        this.color = color;
        return this;
    }

    public LineRequest upStationId(Long upStationId) {
        this.upStationId = upStationId;
        return this;
    }

    public LineRequest downStationId(Long downStationId) {
        this.downStationId = downStationId;
        return this;
    }

    public LineRequest distance(int distance) {
        this.distance = distance;
        return this;
    }

}
