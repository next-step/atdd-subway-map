package subway.line.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LineUpdateRequest {
    private final String name;
    private final String color;
}
