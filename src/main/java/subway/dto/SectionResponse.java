package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SectionResponse {
    private Long id;
    private int distance;
    private StationResponse upStation;
    private StationResponse downStation;
    private Long lineId;
}
