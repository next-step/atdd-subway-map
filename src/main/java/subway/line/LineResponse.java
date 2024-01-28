package subway.line;

import subway.section.Section;
import subway.station.Station;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private final List<StationResponse> stations = new ArrayList<>();

    public LineResponse() {
    }

    public LineResponse(final Line savedLine) {
        this.id = savedLine.getId();
        this.name = savedLine.getName();
        this.color = savedLine.getColor();

        System.out.println("savedLine.getSections()");
        System.out.println(savedLine.getSections());
        for (Section section:
        savedLine.getSections()) {
            System.out.println(section.toString());

        }
        final Section section = savedLine.getSections().get(0);
        this.stations.add(createStationResponse(section.getUpStation()));
        this.stations.add(createStationResponse(section.getDownStation()));
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

    public List<StationResponse> getStations() {
        return stations;
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
