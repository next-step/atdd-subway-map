package subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import subway.constants.LineConstant;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class LineCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    @NotBlank
    private Long upStationId;

    @NotBlank
    private Long downStationId;

    @NotBlank
    @Min(value = 1L, message = LineConstant.DISTANCE_MIN_MESSAGE)
    private Long distance;
}
