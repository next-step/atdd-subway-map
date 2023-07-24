package subway.line;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineRequest {

    private String name;
    private String color;

    private Long upStationId;

    private Long downStationId;

    private int distance;


    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
