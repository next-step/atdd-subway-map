package subway;
import lombok.Value;

@Value
class CreateLineRequest {
    String name;
    String color;
    Long upStationId;
    Long downStationId;
    Long distance;
}
