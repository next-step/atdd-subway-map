package nextstep.subway.applicaion.mapper.response;

import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Component;

@Component
public class StationResponseMapper implements ResponseMapper<Station, StationResponse> {

    @Override
    public StationResponse map(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
