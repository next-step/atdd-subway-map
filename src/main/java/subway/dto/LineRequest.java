package subway.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LineRequest {
    @NotBlank(message = "name is not blank")
    private String name;
    @NotBlank(message = "color is not blank")
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
