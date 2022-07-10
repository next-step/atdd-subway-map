package nextstep.subway.ui.dto.line;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<LineStation> stations;

    public static LineResponse of(Line line, List<Station> createStations) {
        List<LineStation> stations = createStations.stream()
                                                   .map((station) -> new LineStation(station.getId(), station.getName()))
                                                   .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private LineResponse(final Long id,
                         final String name,
                         final String color,
                         final List<LineStation> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<LineStation> getStations() {
        return stations;
    }

    private static class LineStation {
        private Long id;
        private String name;

        public LineStation(final Long id, final String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
