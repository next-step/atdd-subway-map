package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SectionResponse {
    private Long id;
    private StationResponse upStation;
    private StationResponse downStation;
    private int distance;
}
