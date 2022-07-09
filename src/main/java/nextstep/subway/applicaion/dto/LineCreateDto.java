package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import nextstep.subway.ui.dto.LineRequest;

@Builder
@RequiredArgsConstructor
public class LineCreateDto {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public static LineCreateDto of(LineRequest lineRequest) {
        return LineCreateDto.builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .upStationId(lineRequest.getUpStationId())
                .downStationId(lineRequest.getDownStationId())
                .distance(lineRequest.getDistance())
                .build();
    }
}
