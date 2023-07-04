package subway.packet;

import subway.Station;
import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    public static class Station{
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Station(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    private LineResponse(Long id, String name, String color, List<subway.Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations.stream().map(o -> new Station(o.getId(), o.getName())).collect(Collectors.toList());
    }

    public static LineResponse fromEntity(Line line){
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(line.getUpStation(), line.getDownStation()));
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

    public List<Station> getStations() {
        return stations;
    }
}
