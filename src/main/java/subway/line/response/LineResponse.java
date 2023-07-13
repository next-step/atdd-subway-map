package subway.line.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.Station;

@Getter
@Builder
public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

}