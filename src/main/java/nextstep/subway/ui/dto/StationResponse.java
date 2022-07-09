package nextstep.subway.ui.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.StationDto;

@Getter
@RequiredArgsConstructor
public class StationResponse {
    private final Long id;
    private final String name;

    public static StationResponse of(StationDto stationDto) {
        return new StationResponse(stationDto.getId(), stationDto.getName());
    }
}
