package nextstep.subway.applicaion.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LineCreationRequest {
    @NotBlank
    private final String name;
    @NotBlank
    private final String color;
    @NotNull
    private final Long upStationId;
    @NotNull
    private final Long downStationId;
    @Min(1)
    private final Long distance;

    public LineCreationRequest(String name, String color, Long upStationId,
            Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionCreationRequest getSectionCreationRequest() {
        return new SectionCreationRequest(downStationId, upStationId, distance);
    }
}
