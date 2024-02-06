package subway.line;

import subway.section.Section;
import subway.station.Station;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private List<StationResponse> stations = new ArrayList<>();

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        orderStations(line);
    }

    private void orderStations(Line line) {
        Station upStation = line.getUpStation();
        this.stations.add(new StationResponse(upStation));

        for (int i = 0; i < line.getSections().size(); i++) {
            upStation = findNextStation(line, upStation);
            this.stations.add(new StationResponse(upStation));
        }
    }

    private Station findNextStation(Line line, Station upStation) {
        for (Section section : line.getSections()) {
            if (section.getUpStation().equals(upStation)) {
                return section.getDownStation();
            }
        }
        return null;
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
}
