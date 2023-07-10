package subway.model.station;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StationDTO {
    private Long id;
    private String name;

    public static StationDTO from(Station station) {
        return new StationDTO(station.getId(), station.getName());
    }
}
