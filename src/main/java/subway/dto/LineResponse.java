package subway.dto;

import lombok.Getter;
import subway.domain.Line;
import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    List<Station> stations;

    public static LineResponse fromEntity(Line line) {
        List<Station> stations = new ArrayList<>();
        stations.add(line.getUpStation());
        stations.add(line.getDownStation());

        LineResponse lineResponse = new LineResponse();
        lineResponse.id = line.getId();
        lineResponse.name = line.getName();
        lineResponse.color = line.getColor();
        lineResponse.stations = stations;

        return lineResponse;
    }
}
