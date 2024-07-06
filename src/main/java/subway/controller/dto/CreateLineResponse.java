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
}
