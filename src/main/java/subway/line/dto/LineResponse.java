package subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.station.Station;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;
}
