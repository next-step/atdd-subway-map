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
    private long upStationId;

    @NotNull
    @Positive
    private long downStationId;

    @NotNull
    @PositiveOrZero
    private int distance;

    public List<Long> getStationIds() {
        return List.of(upStationId, downStationId);
    }

}
