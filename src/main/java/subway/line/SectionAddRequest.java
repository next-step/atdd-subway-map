package subway.line;

import lombok.*;

@Getter
public class SectionAddRequest {

    private Long upStationId;
    private Long downStationId;
    private int distance;
}
