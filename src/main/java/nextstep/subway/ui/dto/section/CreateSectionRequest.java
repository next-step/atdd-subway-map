package nextstep.subway.ui.dto.section;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateSectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Integer distance;
}
