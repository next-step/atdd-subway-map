package nextstep.subway.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Section;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class SectionResponse {
    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public static SectionResponse of(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getUpStationId(),
                section.getDownStationId(),
                section.getDistance(),
                section.getCreatedDate(),
                section.getModifiedDate());
    }
}
