package subway.station.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import subway.station.entity.Station;

@Builder
@Getter
@AllArgsConstructor
public class StationResponseDto {

    private Long id;

    private String name;

    public static StationResponseDto of(Station station) {
        return StationResponseDto.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }

}
