package nextstep.subway.applicaion.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class StationCreationRequest {

    @NotBlank
    private String name;

}
