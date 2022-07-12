package nextstep.subway.ui.dto.section;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateSectionRequest {
    private long downStationId;
    private long upStationId;
    private int distance;
}
