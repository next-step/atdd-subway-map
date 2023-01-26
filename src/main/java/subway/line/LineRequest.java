package subway.line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineRequest {
    private String name;
    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;
}
