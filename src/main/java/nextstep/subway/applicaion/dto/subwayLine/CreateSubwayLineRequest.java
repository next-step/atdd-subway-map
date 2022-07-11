package nextstep.subway.applicaion.dto.subwayLine;

public class CreateSubwayLineRequest {

    private final String name;

    private final String color;

    private final Long upStationId;

    private final Long downStationId;

    private final Integer distance;

    private CreateSubwayLineRequest() {
        name = null;
        color = null;
        upStationId = null;
        downStationId = null;
        distance = null;
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

    public Integer getDistance() {
        return distance;
    }
}
