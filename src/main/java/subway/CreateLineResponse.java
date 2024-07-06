package subway;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateLineResponse {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
}
