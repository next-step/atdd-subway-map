package nextstep.subway.ui.dto.line;

import lombok.Getter;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<LineStation> stations;

    private LineResponse(final Long id,
                         final String name,
                         final String color,
                         final List<LineStation> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        List<LineStation> stations = line.getSections().getStations().stream()
                                         .map(LineStation::of)
                                         .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    @Getter
    private static class LineStation {
        private final Long id;
        private final String name;

        private LineStation(final Long id, final String name) {
            this.id = id;
            this.name = name;
        }

        public static LineStation of(Station station) {
            return new LineStation(station.getId(), station.getName());
        }
    }
}
