package nextstep.subway.applicaion.dto.line;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class CreateLineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<LineStation> stations;

    public static CreateLineResponse of(Line line, List<Station> createStations) {
        List<LineStation> stations = createStations.stream()
                                                   .map((station) -> new LineStation(station.getId(), station.getName()))
                                                   .collect(Collectors.toList());
        return new CreateLineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private CreateLineResponse(final Long id,
                               final String name,
                               final String color,
                               final List<LineStation> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    private static class LineStation {
        private Long id;
        private String name;

        public LineStation(final Long id, final String name) {
            this.id = id;
            this.name = name;
        }
    }
}
