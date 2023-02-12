package subway.line.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SectionResponse {

    private Long lineId;
    private Long firstSectionId;
    private Long lastSectionId;

}
