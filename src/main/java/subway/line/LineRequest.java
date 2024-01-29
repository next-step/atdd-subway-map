package subway.line;

import org.springframework.beans.factory.annotation.Value;

public class LineRequest {
    private String lineName;
    private String lineColor;
    private Long upStationId;
    private Long downStationId;
    private int distance;
}
