package nextstep.subway.applicaion.line.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.station.Station;

import java.util.List;

@Getter
@Builder
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;
}
