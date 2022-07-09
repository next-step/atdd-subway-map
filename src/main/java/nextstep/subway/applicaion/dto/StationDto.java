package nextstep.subway.applicaion.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.station.Station;

@Getter
@Builder
@RequiredArgsConstructor
public class StationDto {

    private final Long id;
    private final String name;

    public static StationDto of(Station station) {
        return StationDto.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }

}
