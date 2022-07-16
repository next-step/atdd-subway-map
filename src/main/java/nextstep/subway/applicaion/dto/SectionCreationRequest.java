package nextstep.subway.applicaion.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SectionCreationRequest {
    @NotNull
    private final Long downStationId;
    @NotNull
    private final Long upStationId;
    @Min(1)
    private final Long distance;
}
