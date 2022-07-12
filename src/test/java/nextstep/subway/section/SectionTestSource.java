package nextstep.subway.section;

import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.domain.Section;

import static nextstep.subway.line.LineTestSource.lineId;

public class SectionTestSource {

    public static Section section(final Long upStationId) {
        return Section.builder()
                .lineId(lineId)
                .upStationId(upStationId)
                .downStationId(8L)
                .distance(10L)
                .build();
    }

    public static SectionRequest sectionRequest() {
        return SectionRequest.builder()
                .upStationId(6L)
                .downStationId(8L)
                .distance(10L)
                .build();
    }

}
