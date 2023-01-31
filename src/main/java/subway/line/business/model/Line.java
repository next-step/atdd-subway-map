package subway.line.business.model;

import lombok.Builder;
import lombok.Getter;
import subway.station.business.model.Station;

import java.util.List;

@Getter
@Builder
public class Line {

    private Long id;
    private String name;
    private String color;
    private Station upStation;
    private Station downStation;
    private Integer distance;

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }

}
