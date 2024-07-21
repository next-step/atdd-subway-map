package subway.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public final class SubwayLineUpdateRequest {
    private final String name;
    private final String color;
}
