package subway.section;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;
}
