package nextstep.subway.applicaion.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SectionRequest {

    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @NotNull
    private Integer distance;
}
