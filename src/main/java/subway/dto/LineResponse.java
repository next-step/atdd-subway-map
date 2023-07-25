package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.entity.Station;

import java.util.List;

@AllArgsConstructor
@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;
    private int distance;
}
