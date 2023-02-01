package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Station;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SectionResponse {
    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;
}
