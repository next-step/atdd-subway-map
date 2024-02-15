package subway.acceptance;

import java.util.List;
import lombok.Getter;
import subway.station.Station;

@Getter
public class LineResponse {


    @Getter
    static class StationDto {

        private final Long id;

        private final String name;

        public StationDto(Station station) {
            this.id = station.getId();
            this.name = station.getName();
        }
    }


    private final Long id;

    private final String name;

    private final String color;

    private final List<StationDto> stations;

    private final Long distance;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.distance = line.getDistance();
        this.stations = List.of(
            new StationDto(line.getUpStation()),
            new StationDto(line.getDownStation())
        );
    }


}
