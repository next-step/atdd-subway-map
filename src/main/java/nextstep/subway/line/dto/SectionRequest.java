package nextstep.subway.line.dto;

import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.common.exception.ApplicationType;
import nextstep.subway.line.domain.Section;
import org.springframework.util.StringUtils;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() { return this.upStationId; }

    public Long getDownStationId() { return this.downStationId; }

    public int getDistance() { return this.distance; }

    public void validate() {

        if (this.upStationId == null || this.downStationId == null) {
            throw new ApplicationException(ApplicationType.INVALID_REQUEST_PARAMETER);
        }
    }

    public Section toSection() {
        return new Section(this.upStationId, this.downStationId, this.distance);
    }
}
