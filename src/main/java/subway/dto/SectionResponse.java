package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SectionResponse {
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;
}
