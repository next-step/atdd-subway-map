package nextstep.subway.ui.dto.section;

import lombok.Getter;
import nextstep.subway.domain.Section;

@Getter
public class SectionResponse {
    private final long upStationId;
    private final long downStationId;
    private final long lineId;

    private SectionResponse(final long upStationId, final long downStationId, final long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getUpStation().getId(),
                                   section.getDownStation().getId(),
                                   section.getLine().getId());
    }
}
