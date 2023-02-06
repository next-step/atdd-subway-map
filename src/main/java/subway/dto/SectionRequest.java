package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
}
