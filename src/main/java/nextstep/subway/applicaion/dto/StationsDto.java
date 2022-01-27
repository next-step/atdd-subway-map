package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Station;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationsDto {
    private Station upStation;
    private Station downStation;

    public StationsDto(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }
}
