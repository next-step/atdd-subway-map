package nextstep.subway.ui.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionDto;

@Getter
@Builder
@RequiredArgsConstructor
public class SectionResponse {

    private final Long id;

    public static SectionResponse of(SectionDto sectionDto) {
        return SectionResponse.builder()
                .id(sectionDto.getId())
                .build();
    }
}
