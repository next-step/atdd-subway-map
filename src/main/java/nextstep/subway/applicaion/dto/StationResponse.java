package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.subway.domain.station.Station;

@NoArgsConstructor
@Getter
public class StationResponse {

    private Long id;
    private String name;

    public StationResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }

}
