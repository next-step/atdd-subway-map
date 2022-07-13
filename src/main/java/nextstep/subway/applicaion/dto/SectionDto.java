package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.line.Section;

@Getter
@Builder
@RequiredArgsConstructor
public class SectionDto {

    private final Long id;

    public static SectionDto of(Section section) {
        return SectionDto.builder()
                .id(section.getId())
                .build();
    }
}
