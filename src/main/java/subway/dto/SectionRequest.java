package subway.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class SectionRequest {
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;
}
