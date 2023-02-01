package subway.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @Min(value = 0)
    private int distance;

    public LineRequest() {
    }

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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public List<Long> getStationIds() {
        return List.of(upStationId, downStationId);
    }

    public int getDistance() {
        return distance;
    }
}
