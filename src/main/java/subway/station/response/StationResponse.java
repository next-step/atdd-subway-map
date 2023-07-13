package subway.station.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationResponse {

    private final Long id;
    private final String name;

}