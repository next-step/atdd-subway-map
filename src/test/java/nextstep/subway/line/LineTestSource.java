package nextstep.subway.line;

import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.domain.Line;

import static nextstep.subway.section.SectionTestSource.section;

public class LineTestSource {

    public static final Long lineId = 1L;

    public static LineRequest lineRequest() {
        return LineRequest.builder()
                .name("lineName")
                .color("bg-red-600")
                .upStationId(2L)
                .downStationId(3L)
                .distance(4L)
                .build();
    }

    public static Line line() {
        return Line.builder()
                .id(lineId)
                .name("lineName")
                .color("bg-red-600")
                .build();
    }

    public static Line line(final Long lineId) {
        return Line.builder()
                .id(lineId)
                .name("lineName")
                .color("bg-red-600")
                .build();
    }

    public static Line lineWithSection() {
        final Line line = Line.builder()
                .id(lineId)
                .name("lineName")
                .color("bg-red-600")
                .build();

        line.addSection(section());
        return line;
    }

}
