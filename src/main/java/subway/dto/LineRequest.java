package subway.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotBlank
    private final String name;
    @NotBlank
    private final String color;
    @NotNull
    private final Long upStationId;
    @NotNull
    private final Long downStationId;
    @Min(value = 0)
    private final int distance;

    public LineRequest(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Long> getStationIds() {
        return List.of(upStationId, downStationId);
    }

    public int getDistance() {
        return distance;
    }
}
