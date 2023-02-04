package subway.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class SectionRequest {

    @NotBlank
    private Long lineId;

    @NotBlank
    private Long upStationId;

    @NotBlank
    private Long downStationId;

    @NotBlank
    private Integer distance;
}
