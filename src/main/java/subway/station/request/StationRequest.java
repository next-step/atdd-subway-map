package subway.station.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationRequest {

    private final String name;

}