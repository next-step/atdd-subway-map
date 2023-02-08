package subway.dto.line;

import lombok.Getter;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReadLinesResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<ReadLinesStationResponse> stations = new ArrayList<>();


    public ReadLinesResponse(Line line) {
        this.id = line.getId();
        this.name = line.getNameValue();
        this.color = line.getColor().getName();

        List<Station> stations = line.getStationsByAscendingOrder();
        for (Station station : stations) {
            this.stations.add(new ReadLinesStationResponse(station.getId(), station.getNameValue()));
        }
    }


}
