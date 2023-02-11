package subway.line.section;

import lombok.*;

@Getter
public class SectionAddRequest {

    private Long upStationId;
    private Long downStationId;
    private int distance;
}
