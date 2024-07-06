package subway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class CreateLineResponse {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public static CreateLineResponse from(CreateLineRequest request) {
        return new CreateLineResponse(
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
        );
    }
}
