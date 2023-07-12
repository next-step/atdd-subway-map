package subway.dto;

import lombok.Getter;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    List<Station> stations;

    public static LineResponse fromEntity(Line line) {
        LineResponse lineResponse = new LineResponse();
        lineResponse.id = line.getId();
        lineResponse.name = line.getName();
        lineResponse.color = line.getColor();
        lineResponse.stations = line.getStationList();

        return lineResponse;
    }
}
