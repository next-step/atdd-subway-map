package subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import subway.constants.LineConstant;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class LineModifyRequest {

    @NotBlank(message = LineConstant.NAME_NOT_BLANK_MODIFY_MESSAGE)
    private String name;

    private String color;
}
