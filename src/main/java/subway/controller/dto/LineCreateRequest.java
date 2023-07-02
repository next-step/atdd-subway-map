package subway.controller.dto;

import subway.exception.LineNotEstablishedBySameEndStationException;

public class LineCreateRequest {

    private final String name;

    private final String color;

    private final Long upStationId;

    private final Long downStationId;

    private final Long distance;

    public LineCreateRequest(String name, String color, Long upStationId, Long downStationId,
        Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = valid(downStationId);
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

    private Long valid(Long downStationId) {
        if (isSameWithUpStationId(downStationId)) {
            throw new LineNotEstablishedBySameEndStationException(
                String.format("노선의 두 종점역은 동일할 수 없습니다: %d / %d", upStationId, downStationId)
            );
        }
        return downStationId;
    }

    private boolean isSameWithUpStationId(Long downStationId) {
        return downStationId.equals(upStationId);
    }
}
