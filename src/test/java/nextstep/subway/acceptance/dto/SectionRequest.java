package nextstep.subway.acceptance.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SectionRequest {
    private Long lineId;
    private String upStationName;
    private String downStationName;
    private int distance;

    @Builder
    private SectionRequest(Long lineId, String upStationName,
                          String downStationName, int distance) {
        this.lineId = lineId;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }
}
