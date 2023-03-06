package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionRequest {

    private Long downStationId; // 새로운 구간의 하행역
    private Long upStationId; // 새로운 구간의 상행역
    private int distance;
}
