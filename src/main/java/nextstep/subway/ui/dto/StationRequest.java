package nextstep.subway.ui.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.StationCreateDto;

@Getter
@RequiredArgsConstructor
public class StationRequest {
    private final String name;

    public StationCreateDto toDto() {
        return new StationCreateDto(this.name);
    }
}
