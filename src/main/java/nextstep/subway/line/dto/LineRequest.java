package nextstep.subway.line.dto;

import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.common.exception.ApplicationType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.springframework.util.StringUtils;

public class LineRequest {
    private String name;
    private String color;
    private int distance;
    private Long upStationId;       // 추가
    private Long downStationId;     // 추가

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

    public int getDistance() {return this.distance; }

    public Line toLine(Section section) {
        return new Line(this.name, this.color, section);
    }

    public Line toLine() {
        return new Line(this.name, this.color, null);
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
