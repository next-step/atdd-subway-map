package subway.line;

import lombok.Builder;
import lombok.Getter;

/**
 * StationsLineUpdateRequest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@Builder
@Getter
public class StationsLineUpdateRequest {

    private String name;

    private String color;

    public StationLine toEntity() {
        return StationLine.builder()
                .name(name)
                .color(color)
                .build();
    }
}
