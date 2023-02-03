package subway.station.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.station.Station;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    @Builder
    public SaveLineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }
}
