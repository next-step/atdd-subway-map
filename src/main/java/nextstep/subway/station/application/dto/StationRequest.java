package nextstep.subway.station.application.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class StationRequest {
    @NotBlank
    private String name;
}
