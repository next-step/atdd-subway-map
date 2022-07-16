package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = Stream.of(line.getUpStation(), line.getDownStation())
                .map(s -> new StationResponse(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }
}
