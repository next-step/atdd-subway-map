package subway.presentation.section.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionRequest {

    private String downStationId;

    private String upStationId;

    private Integer distance;

}
