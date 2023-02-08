package subway.dto.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReadLineResponse {
    private Long id;
    private String name;
    private String color;
    private final List<ReadLineStationResponse> stations = new ArrayList<>();

    public ReadLineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getNameValue();
        this.color = line.getColor().getName();

        List<Station> stations = line.getStationsByAscendingOrder();
        for (Station station : stations) {
            this.stations.add(new ReadLineStationResponse(station.getId(), station.getNameValue()));
        }
    }

}
