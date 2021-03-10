package nextstep.subway.line.dto;

import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.common.exception.ApplicationType;
import nextstep.subway.line.domain.Line;
import org.springframework.util.StringUtils;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;       // 추가
    private Long downStationId;     // 추가
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Long getUpStationId() { return this.upStationId; }

    public Long getDownStationId() { return this.downStationId; }

    public Line toLine() {
        return new Line(this.name, this.color, this.upStationId, this.downStationId, this.distance);
    }

    public void validate() {
        if (!StringUtils.hasText(this.name)) {
            throw new ApplicationException(ApplicationType.INVALID_REQUEST_PARAMETER);
        }

        if (!StringUtils.hasText(this.color)) {
            throw new ApplicationException(ApplicationType.INVALID_REQUEST_PARAMETER);
        }

        if (this.downStationId == null) {
            throw new ApplicationException(ApplicationType.INVALID_REQUEST_PARAMETER);
        }
    }
}
