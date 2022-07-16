package nextstep.subway.applicaion.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StationRequest {
    @NotBlank
    private String name;
}
