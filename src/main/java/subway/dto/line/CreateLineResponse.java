package subway.dto.line;

import lombok.Getter;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateLineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<CreateLineStationResponse> stations = new ArrayList<>();

    public CreateLineResponse(Line line, List<Station> stations) {
        this.id = line.getId();
        this.name = line.getNameValue();
        this.color = line.getColor().getName();

        for (Station station : stations) {
            this.stations.add(new CreateLineStationResponse(station.getId(), station.getNameValue()));
        }
    }

}
