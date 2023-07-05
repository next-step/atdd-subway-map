package subway;

import lombok.Value;

@Value
public class UpdateLineRequest {
    String name;
    String color;
}
