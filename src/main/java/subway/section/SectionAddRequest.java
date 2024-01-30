package subway.section;

import lombok.Getter;

@Getter
public class SectionAddRequest {
    private Long upstationId;
    private Long downstationId;
    private int distance;
}
