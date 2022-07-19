package nextstep.subway.applicaion.dto;

public class LineRequest {

    private Long id;
    private String name;
    private String color;
    private Long upStationId; // 상행 종점 역
    private Long downStationId; // 하행 종점 역
    private Long distance;

    public Long getId() {
        return id;
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
}
