package nextstep.subway.applicaion.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
public class LineCreationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    @NotNull
    @Positive
    private Long upStationId;

    @NotNull
    @Positive
    private Long downStationId;

    @NotNull
    @PositiveOrZero
    private Integer distance;

    public List<Long> getStationIds() {
        return List.of(upStationId, downStationId);
    }

}
