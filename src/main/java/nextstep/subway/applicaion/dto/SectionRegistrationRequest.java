package nextstep.subway.applicaion.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
public class SectionRegistrationRequest {

    @NotNull
    @Positive
    private long upStationId;

    @NotNull
    @Positive
    private long downStationId;

    @NotNull
    @PositiveOrZero
    private int distance;

}
