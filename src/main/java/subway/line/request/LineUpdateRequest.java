package subway.line.request;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class LineUpdateRequest {
    private String name;
    private String color;
}
