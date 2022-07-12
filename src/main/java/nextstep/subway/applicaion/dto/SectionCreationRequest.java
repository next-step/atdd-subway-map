package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SectionCreationRequest {
    private final Long downStationId;
    private final Long upStationId;
    private final Long distance;
}
