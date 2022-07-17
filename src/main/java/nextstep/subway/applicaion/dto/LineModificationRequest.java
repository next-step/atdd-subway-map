package nextstep.subway.applicaion.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LineModificationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

}
