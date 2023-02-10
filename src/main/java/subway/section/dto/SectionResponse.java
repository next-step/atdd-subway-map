package subway.section.dto;

import lombok.Builder;
import lombok.Getter;
import subway.section.domain.Section;

@Getter
public class SectionResponse {

    private Long Id;

    @Builder
    private SectionResponse(Long id) {
        Id = id;
    }

    public static SectionResponse fromDomain(Section section) {
        return null;
    }
}