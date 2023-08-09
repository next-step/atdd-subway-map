package subway.section.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionResponse {
    private Long upStationId;
    private Long downStationId;
    private int distance;
}
