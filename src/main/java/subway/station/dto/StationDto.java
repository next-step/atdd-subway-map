package subway.station.dto;

import lombok.Getter;
import subway.station.entity.Station;

@Getter
public class StationDto {

    private Long id;

    private String name;

    public StationDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationDto(String name) {
        this.name = name;
    }

    public static StationDto from(Station station) {
        return new StationDto(
                station.getId(),
                station.getName()
        );
    }
}
