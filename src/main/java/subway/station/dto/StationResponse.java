package subway.station.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class StationResponse {
    private final Long id;
    private final String name;
}
