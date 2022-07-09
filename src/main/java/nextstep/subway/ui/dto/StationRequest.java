package nextstep.subway.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.StationCreateDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StationRequest {
    private String name;

    public StationCreateDto toDto() {
        return new StationCreateDto(this.name);
    }
}
