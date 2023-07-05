package subway.station.dto;

import lombok.Getter;
import subway.constants.StationConstant;

import javax.validation.constraints.NotBlank;

@Getter
public class StationRequest {

    @NotBlank(message = StationConstant.NAME_NOT_BLANK_MESSAGE)
    private String name;
}
