package subway.interfaces.station.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StationResponse {
    private final Long id;
    private final String name;
}
