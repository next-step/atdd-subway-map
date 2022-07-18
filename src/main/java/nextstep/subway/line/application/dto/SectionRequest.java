package nextstep.subway.line.application.dto;

import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
public class SectionRequest {
    @Min(1)
    private long upStationId;
    @Min(1)
    private long downStationId;
    @Min(0)
    private int distance;
}
