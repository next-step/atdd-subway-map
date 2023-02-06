package subway.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.station.Station;

@Getter
@AllArgsConstructor
public class UpAndDownStationsVo {
    private final Station upStation;
    private final Station downStation;

}
