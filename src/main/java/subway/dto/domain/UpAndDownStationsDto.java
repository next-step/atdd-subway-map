package subway.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.station.Station;

@Getter
@AllArgsConstructor
public class UpAndDownStationsDto {
    private Station upStation;
    private Station downStation;

}
