package subway;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class LineRequest {
    @Size(min = 0, max = 20)
    private String color;
    @Size(min = 0, max = 20)
    private String name;

    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;
    @Positive
    private Long distance;

    public String getName() {
        return name;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public String getColor() {
        return color;
    }
}
