package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.subway.domain.station.Station;
import nextstep.subway.utils.ObjectMapUtils;

@Getter
@Setter
@NoArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private SectionsResponse sections;
    private StationResponse upStation;
    private StationResponse downStation;

    public void sideStation(Station upStation, Station downStation) {
        this.upStation = ObjectMapUtils.map(upStation, StationResponse.class);
        this.downStation = ObjectMapUtils.map(downStation, StationResponse.class);
    }


}
