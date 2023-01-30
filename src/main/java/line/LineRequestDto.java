package line;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineRequestDto {
    String name;
    String color;
    Long upStationId;
    Long downStationId;
    Long distance;
}
