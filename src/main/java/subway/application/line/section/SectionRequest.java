package subway.application.line.section;

import lombok.Getter;

@Getter
public class SectionRequest {

    Long downStationId;
    Long upStationId;
    Integer distance;

}
