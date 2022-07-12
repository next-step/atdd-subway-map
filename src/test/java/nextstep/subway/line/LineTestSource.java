package nextstep.subway.line;

import nextstep.subway.line.domain.Line;

public class LineTestSource {

    public static final Long lineId = 1L;

    public static Line line() {
        return Line.builder()
                .id(lineId)
                .name("lineName")
                .color("bg-red-600")
                .upStationId(2L)
                .downStationId(3L)
                .distance(4L)
                .build();
    }

}
