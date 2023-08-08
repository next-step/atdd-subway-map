package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionAddRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
}
