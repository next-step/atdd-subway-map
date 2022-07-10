package nextstep.subway.applicaion.dto.stationLine;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;

import java.util.List;
import java.util.stream.Collectors;

public class CreateStationLineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<LineStation> stations;

    public static CreateStationLineResponse of(StationLine stationLine, List<Station> createStations) {
        List<LineStation> stations = createStations.stream()
                                                   .map((station) -> new LineStation(station.getId(), station.getName()))
                                                   .collect(Collectors.toList());
        return new CreateStationLineResponse(stationLine.getId(), stationLine.getName(), stationLine.getColor(), stations);
    }

    private CreateStationLineResponse(final Long id,
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
