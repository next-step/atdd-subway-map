package subway;
import lombok.Value;

@Value
class LineRequest {
    String name;
    String color;
    Long upStationId;
    Long downStationId;
    Long distance;
}
