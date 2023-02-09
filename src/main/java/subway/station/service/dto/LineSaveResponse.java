package subway.station.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class LineSaveResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations = new ArrayList<>();

    @Builder
    public LineSaveResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }
}
