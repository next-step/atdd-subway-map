package subway.line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import subway.section.StationSection;
import subway.station.StationResponse;

public class StationLineResponse {
    private long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public StationLineResponse(StationLine stationLine) {
        this.id = stationLine.getId();
        this.name = stationLine.getName();
        this.color = stationLine.getColor();
        this.stations = getStationsResponse(stationLine);

    }

    private List<StationResponse> getStationsResponse(StationLine stationLine) {
        List<StationResponse> stations = new ArrayList<>();

        for (StationSection stationSection : stationLine.getStationSections()) {
            stations.add(new StationResponse(stationSection.getUpStation()));
            stations.add(new StationResponse(stationSection.getDownStation()));
        }

        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
