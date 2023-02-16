package subway.line.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SectionRequest {

    private Long downStationId;
    private Long upStationId;
    private int distance;

    public SectionRequest(Long downStationId, Long upStationId, int distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

}
