package subway.lines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import subway.station.Station;
import subway.station.StationResponse;

public class LinesResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations = new ArrayList<>();

    public LinesResponse() {

    }

    public LinesResponse(Lines lines) {
        this.id = lines.getId();
        this.name = lines.getName();
        this.color = lines.getColor();
        this.stations = Arrays.stream(new Station[]{lines.getUpStation(), lines.getDownStation()})
            .map(StationResponse::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public String getColor() {
        return color;
    }
}
