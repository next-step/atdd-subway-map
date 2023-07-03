package subway;

import lombok.Value;

import java.util.List;

@Value
public class LineResponse {
    Long id;
    String name;
    String color;
    List<StationResponse> stations;
}
