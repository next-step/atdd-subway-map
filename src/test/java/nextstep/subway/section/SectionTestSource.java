package nextstep.subway.section;

import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.domain.Section;

import static nextstep.subway.line.LineTestSource.line;

public class SectionTestSource {

    public static Section section(final Long upStationId) {
        return Section.builder()
                .line(line())
                .upStationId(upStationId)
                .downStationId(8L)
                .distance(10L)
                .build();
    }

    public static Section section(final Long upStationId, final Long downStationId) {
        return Section.builder()
                .line(line())
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(10L)
                .build();
    }

    public static Section section() {
        return Section.builder()
                .upStationId(8L)
                .downStationId(9L)
                .distance(10L)
                .build();
    }

    public static SectionRequest sectionRequest() {
        return SectionRequest.builder()
                .upStationId(8L)
                .downStationId(9L)
                .distance(10L)
                .build();
    }

    public static SectionRequest sectionRequest(final Long upStationId) {
        return SectionRequest.builder()
                .upStationId(upStationId)
                .downStationId(9L)
                .distance(10L)
                .build();
    }

    public static SectionRequest sectionRequest(final Long upStationId, final Long downStationId) {
        return SectionRequest.builder()
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(10L)
                .build();
    }

}
