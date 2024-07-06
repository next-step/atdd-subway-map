package subway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.entity.Station;

import java.util.List;

@Getter
@AllArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;
}
