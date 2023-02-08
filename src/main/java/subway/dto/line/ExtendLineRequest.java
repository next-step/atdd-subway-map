package subway.dto.line;

import lombok.Getter;

@Getter
public class ExtendLineRequest {
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public ExtendLineRequest setLineId(Long id) {
        this.lineId = id;
        return this;
    }

}
