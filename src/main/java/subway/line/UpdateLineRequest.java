package subway.line;

import lombok.Value;

@Value
class UpdateLineRequest {
    String name;
    String color;
}
